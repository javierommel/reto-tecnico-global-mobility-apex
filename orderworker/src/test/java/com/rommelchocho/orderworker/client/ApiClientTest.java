package com.rommelchocho.orderworker.client;

import java.io.IOException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.test.util.ReflectionTestUtils;

import com.rommelchocho.orderworker.model.Customer;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import reactor.test.StepVerifier;

class ApiClientTest {

    private ApiClient apiClient;
    private MockWebServer server;

    @BeforeEach
    void setup() throws IOException {
        server = new MockWebServer();
        server.start();
        String baseUrl = server.url("/").toString();

        WebClient webClient = WebClient.builder().baseUrl(baseUrl).build();
        apiClient = new ApiClient(webClient);

        // Si usas @Value para baseUrl, asegúrate que la uses correctamente en la clase real
        ReflectionTestUtils.setField(apiClient, "baseUrl", baseUrl);
    }

    @AfterEach
    void shutdown() throws IOException {
        server.shutdown();
    }

    @Test
    void shouldReturnCustomer() {
        String body = """
            {
              "customerId": "customer-1",
              "name": "John",
              "email": "john@example.com",
              "phone": "123456789",
              "active": true
            }
        """;

        server.enqueue(new MockResponse()
            .setBody(body)
            .addHeader("Content-Type", "application/json"));

        StepVerifier.create(apiClient.getCustomer("customer-1"))
            .expectNextMatches((Customer c) -> 
                c.customerId().equals("customer-1") &&
                c.name().equals("John") &&
                c.active())
            .verifyComplete();
    }
}
