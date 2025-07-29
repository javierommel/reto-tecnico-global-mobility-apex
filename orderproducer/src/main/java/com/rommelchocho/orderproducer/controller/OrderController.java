package com.rommelchocho.orderproducer.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rommelchocho.orderproducer.kafka.KafkaProducerService;
import com.rommelchocho.orderproducer.model.OrderMessage;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;


@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {

    private final KafkaProducerService kafkaProducerService;

    @PostMapping("/send")
    public String sendOrder(@RequestParam String customerId, @RequestParam List<String> productIds) {
        OrderMessage order = new OrderMessage("order-" + UUID.randomUUID(),
                customerId,
                productIds);
        kafkaProducerService.sendOrder(order);
        return "Pedido enviado al tópico Kafka";
    }

}
