package com.rommelchocho.orderworker.kafka;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rommelchocho.orderworker.model.OrderMessage;
import com.rommelchocho.orderworker.processor.OrderProcessor;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOptions;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaOrderConsumer {

    private final ReceiverOptions<String, String> receiverOptions;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final OrderProcessor orderProcessor;

    @PostConstruct
    public void counsumeMessages() {
        KafkaReceiver.create(receiverOptions)
                .receive()
                .doOnNext(record -> {
                    try {
                        OrderMessage order = objectMapper.readValue(record.value(), OrderMessage.class);
                        orderProcessor.process(order)
                                .subscribe();
                    } catch (Exception e) {
                        log.error("Error deserealizando mensaje Kafka", e);
                    }
                })
                .subscribe();
    }
}
