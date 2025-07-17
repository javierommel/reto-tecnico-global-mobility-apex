package com.rommelchocho.orderworker.model;

public record Customer(
    String customerId,
    String name,
    String email,
    boolean active
) {

}
