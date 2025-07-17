package com.rommelchocho.orderworker.client;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.rommelchocho.orderworker.model.Customer;
import com.rommelchocho.orderworker.model.Product;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ApiClient {

    private WebClient webClient;

    @Value("${external.api.base-url}")
    private String baseUrl;

    public Mono<Customer> getCustomer(String customerId) {
        return webClient.get()
                .uri(baseUrl + "/api/clientes/{id}", customerId)
                .retrieve()
                .bodyToMono(Customer.class);
    }

    public Mono<List<Product>> getProducts(List<String> productIds) {
        String ids = String.join(",", productIds);

        return webClient.get()
                .uri(baseUrl + "/api/productos?ids=" + ids)
                .retrieve()
                .bodyToFlux(Product.class)
                .collectList();
    }
}
