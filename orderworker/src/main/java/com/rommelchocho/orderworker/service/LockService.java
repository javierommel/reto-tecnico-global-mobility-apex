package com.rommelchocho.orderworker.service;

import java.time.Duration;
import java.util.UUID;

import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class LockService {

    private final ReactiveStringRedisTemplate redisTemplate;
    private static final Duration LOCK_TTL = Duration.ofSeconds(30); // Tiempo de vida del lock

    // Guarda el token del lock para verificar antes de liberar
    private final ThreadLocal<String> lockTokenHolder = new ThreadLocal<>();

    public Mono<Boolean> acquireLock(String key) {
        String token = UUID.randomUUID().toString();
        lockTokenHolder.set(token);

        return redisTemplate.opsForValue()
            .setIfAbsent(key, token, LOCK_TTL)
            .map(acquired -> {
                if (!acquired) {
                    lockTokenHolder.remove(); // limpiar si no se adquirió
                }
                return acquired;
            });
    }

    public Mono<Void> releaseLock(String key) {
        String token = lockTokenHolder.get();
        if (token == null) return Mono.empty();

        return redisTemplate.opsForValue()
            .get(key)
            .flatMap(currentToken -> {
                if (token.equals(currentToken)) {
                    return redisTemplate.delete(key).then();
                } else {
                    return Mono.empty(); // otro proceso tomó el lock
                }
            })
            .doFinally(signal -> lockTokenHolder.remove());
    }
}
