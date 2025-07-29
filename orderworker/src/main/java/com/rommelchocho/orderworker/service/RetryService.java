package com.rommelchocho.orderworker.service;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rommelchocho.orderworker.model.FailedOrder;
import com.rommelchocho.orderworker.model.OrderMessage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class RetryService {

    private final ReactiveStringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper= new ObjectMapper();

    @Value("${retry.max-attempts:5}")
    private int maxAttempts;

    public Mono<Void> handleRetry(OrderMessage order, Throwable error) {
        String key = "retry:order:" + order.orderId();
        log.info("Retry-Key: {} ", key);
        return redisTemplate.opsForValue()
            .get(key)
            .flatMap(json -> {
                try {
                    log.info("ingresa primero");
                    log.info("json: {}", json);
                    FailedOrder failed = objectMapper.readValue(json, FailedOrder.class);
                    failed.setAttempts(failed.getAttempts() + 1);
                    failed.setLastError(error.getMessage());
                    return Mono.just(failed);
                } catch (Exception e) {
                    log.info("Error al convertir el JSON a objeto FailedOrder: {}", e.getMessage());
                    return Mono.error(e);
                }
            })
            .switchIfEmpty(Mono.just(new FailedOrder(order, 1, error.getMessage())))
            .flatMap(failed -> {
                if (failed.getAttempts() >= maxAttempts) {
                    log.error("Pedido " + order.orderId() + " marcado como fallido.");
                    return redisTemplate.opsForValue()
                        .set(key, toJson(failed), Duration.ofHours(24))
                        .then(); // no reintenta más
                }
                Duration retryDelay = getExponentialBackoff(failed.getAttempts());
                return redisTemplate.opsForValue()
                    .set(key, toJson(failed), retryDelay)
                    .doOnError(e -> log.error("ERROR CRÍTICO: Fallo al guardar en Redis (reintento) para clave {}: {}", key, e.getMessage(), e)) // <-- AÑADE doOnError AQUÍ
                    .then(Mono.delay(retryDelay).then(Mono.empty())); // solo deja que reintente en la próxima ronda
            });
    }

    private Duration getExponentialBackoff(int attempt) {
        return Duration.ofSeconds((long) Math.pow(2, attempt)); // 2, 4, 8, 16, etc.
    }

    private String toJson(FailedOrder failed) {
        try {
            return objectMapper.writeValueAsString(failed);
        } catch (Exception e) {
            throw new RuntimeException("JSON error", e);
        }
    }
}
