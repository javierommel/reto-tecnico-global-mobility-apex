package com.rommelchocho.orderworker.client;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.rommelchocho.orderworker.model.Customer;
import com.rommelchocho.orderworker.model.Product;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class ApiClient {

    private final WebClient webClient;

    @Value("${external.api.base-url}")
    private String baseUrl;

    public Mono<Customer> getCustomer(String customerId) {
        log.info("url apiclient: {}",(baseUrl + "/api/clientes/{id}" + customerId));
        return webClient.get()
                .uri(baseUrl + "/api/clientes/{id}", customerId)
                .retrieve()
                .bodyToMono(Customer.class);
    }

    public Mono<List<Product>> getProducts(List<String> productIds) {
        String ids = String.join(",", productIds);
        log.info("url apiclient: {}",(baseUrl + "/api/productos?ids=" + ids));
        return webClient.get()
                .uri(baseUrl + "/api/productos?ids=" + ids)
                .retrieve()
                .bodyToFlux(Product.class)
                .collectList();
    }
}
