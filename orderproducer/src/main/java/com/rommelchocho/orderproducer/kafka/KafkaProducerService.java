package com.rommelchocho.orderproducer.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.rommelchocho.orderproducer.model.OrderMessage;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KafkaProducerService {

    private static final String TOPIC = "orders-topic";
    private final KafkaTemplate<String, OrderMessage> kafkaTemplate;

    public void sendOrder(OrderMessage order) {
        kafkaTemplate.send(TOPIC, order.orderId(), order);
    }

}
