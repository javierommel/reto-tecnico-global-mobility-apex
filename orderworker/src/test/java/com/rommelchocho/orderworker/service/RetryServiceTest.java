package com.rommelchocho.orderworker.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.time.Duration;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.data.redis.core.ReactiveValueOperations;
import org.springframework.test.util.ReflectionTestUtils;

import com.rommelchocho.orderworker.model.OrderMessage;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class RetryServiceTest {

    private RetryService retryService;

    @Mock
    private ReactiveStringRedisTemplate redis;

    @Mock
    private ReactiveValueOperations<String, String> valOps;

    @BeforeEach
    void setup() {
        retryService = new RetryService(redis);
        ReflectionTestUtils.setField(retryService, "maxAttempts", 3);

        when(redis.opsForValue()).thenReturn(valOps);
    }

    
    void shouldStoreRetryInRedis() {
        // Arrange
        OrderMessage msg = new OrderMessage("order-retry", "cust", List.of("prod"));

        when(valOps.get(anyString())).thenReturn(Mono.empty()); // Simula que no hay reintentos previos
        when(valOps.set(anyString(), anyString(), any(Duration.class))).thenReturn(Mono.just(true));

        // Act & Assert
        StepVerifier.create(retryService.handleRetry(msg, new RuntimeException("fail")))
            .verifyComplete();
    }
}

