package com.rommelchocho.orderworker.processor;

import java.util.List;

import org.springframework.stereotype.Service;

import com.rommelchocho.orderworker.client.ApiClient;
import com.rommelchocho.orderworker.model.Customer;
import com.rommelchocho.orderworker.model.EnrichedOrder;
import com.rommelchocho.orderworker.model.OrderMessage;
import com.rommelchocho.orderworker.model.Product;
import com.rommelchocho.orderworker.repository.OrderRepository;
import com.rommelchocho.orderworker.service.LockService;
import com.rommelchocho.orderworker.service.RetryService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
@Slf4j
public class OrderProcessor {

    private final ApiClient apiClient;
    private final OrderRepository orderRepository;
    private final RetryService retryService;
    private final LockService lockService;

    public Mono<Void> process(OrderMessage orderMessage) {
        String lockKey = "lock:order:" + orderMessage.orderId();
        log.info("key: {}",lockKey);
        return lockService.acquireLock(lockKey)
            .flatMap(acquired -> {
                if (!acquired) {
                    log.info("Warning: Otro worker está procesando");
                    return Mono.empty();  
                }
                log.info("Paso sin bloqueo");
                return processInternal(orderMessage)
                    .onErrorResume(error -> retryService.handleRetry(orderMessage, error))
                    .doFinally(signal -> lockService.releaseLock(lockKey));
            });
    }

    private Mono<Void> processInternal(OrderMessage orderMessage) {
        return Mono.zip(
                apiClient.getCustomer(orderMessage.customerId()),
                apiClient.getProducts(orderMessage.productIds())
        ).flatMap(tuple -> {
            Customer customer = tuple.getT1();
            List<Product> products = tuple.getT2();

            if (!customer.active() || products.size() != orderMessage.productIds().size()) {
                return Mono.error(new RuntimeException("Datos inválidos"));
            }

            EnrichedOrder enrichedOrder = new EnrichedOrder();
            enrichedOrder.setOrderId(orderMessage.orderId());
            enrichedOrder.setCustomerId(orderMessage.customerId());
            enrichedOrder.setProducts(
                products.stream()
                        .map(p -> new EnrichedOrder.EnrichedProduct(p.productId(), p.name(), p.price()))
                        .toList()
            );

            return orderRepository.save(enrichedOrder).then();
        });
    }
}
