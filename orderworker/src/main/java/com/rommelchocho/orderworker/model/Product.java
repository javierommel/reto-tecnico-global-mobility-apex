package com.rommelchocho.orderworker.model;

public record Product(
                String productId,
                String name,
                String descripcion,
                double price,
                boolean active) {

}
