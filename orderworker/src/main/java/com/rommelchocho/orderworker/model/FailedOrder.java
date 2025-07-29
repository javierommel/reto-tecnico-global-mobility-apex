package com.rommelchocho.orderworker.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FailedOrder {
    private OrderMessage order;
    private int attempts;
    private String lastError;
}
