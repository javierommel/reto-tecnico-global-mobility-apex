package com.rommelchocho.orderworker.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FailedOrder {
    private OrderMessage order;
    private int attempts;
    private String lastError;
}
