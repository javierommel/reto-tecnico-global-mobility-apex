package com.rommelchocho.orderworker.processor;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.rommelchocho.orderworker.client.ApiClient;
import com.rommelchocho.orderworker.model.Customer;
import com.rommelchocho.orderworker.model.EnrichedOrder;
import com.rommelchocho.orderworker.model.OrderMessage;
import com.rommelchocho.orderworker.model.Product;
import com.rommelchocho.orderworker.repository.OrderRepository;
import com.rommelchocho.orderworker.service.LockService;
import com.rommelchocho.orderworker.service.RetryService;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
public class OrderProcessorTest {

    @Mock
    ApiClient apiClient;

    @Mock
    OrderRepository orderRepository;

    @Mock
    RetryService retryService;

    @Mock
    LockService lockService;

    @InjectMocks
    OrderProcessor orderProcessor;

    @Test
    void shouldProcessValidOrder() {
        OrderMessage msg = new OrderMessage("order-1", "customer-1", List.of("product-1"));

        when(lockService.acquireLock("lock:order:order-1")).thenReturn(Mono.just(true));
        when(apiClient.getCustomer("customer-1"))
                .thenReturn(Mono.just(new Customer("customer-1", "John", "john@x.com", "111111111",true)));

        when(apiClient.getProducts(List.of("product-1")))
                .thenReturn(Mono.just(List.of(new Product("product-1", "Laptop", "Desc", 999.99, true))));

        when(orderRepository.save(any())).thenReturn(Mono.just(new EnrichedOrder()));

        when(lockService.releaseLock(any())).thenReturn(Mono.empty());

        StepVerifier.create(orderProcessor.process(msg))
                .verifyComplete();
    }

    @Test
    void shouldHandleInvalidCustomer() {
        OrderMessage msg = new OrderMessage("order-2", "customer-bad", List.of("product-1"));

        when(lockService.acquireLock(any())).thenReturn(Mono.just(true));
        when(apiClient.getCustomer("customer-bad"))
                .thenReturn(Mono.just(new Customer("customer-bad", "Jane", "x@x.com", "1111111111",false)));

        when(apiClient.getProducts(any()))
                .thenReturn(Mono.just(List.of(new Product("product-1", "Laptop", "Desc", 999.99, true))));

        when(retryService.handleRetry(eq(msg), any())).thenReturn(Mono.empty());

        StepVerifier.create(orderProcessor.process(msg))
                .verifyComplete();
    }
}