package com.rommelchocho.orderworker.model;

import java.util.List;

public record OrderMessage(String orderId,
        String customerId,
        List<String> productIds) {
}
