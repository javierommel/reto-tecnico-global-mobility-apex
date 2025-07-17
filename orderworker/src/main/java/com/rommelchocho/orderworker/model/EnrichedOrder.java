package com.rommelchocho.orderworker.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document("orders")
public class EnrichedOrder {

    @Id
    private String id;

    private String orderId;
    private String customerId;
    private List<EnrichedProduct> products;

    @Data
    public static class EnrichedProduct {
        private String productId;
        private String name;
        private double price;

        public EnrichedProduct(String productId, String name, double price) {
            this.productId = productId;
            this.name = name;
            this.price = price;
        }
    }
}