package com.rommelchocho.orderproducer.model;

import java.util.List;

public record OrderMessage(
    String orderId,
    String customerId,
    List<String> productIds
) {}