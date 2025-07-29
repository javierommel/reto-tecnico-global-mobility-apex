package com.rommelchocho.orderworker.model;

public record Product(
        String productId,
        String name,
        String description,
        double price,
        boolean active) {

}
