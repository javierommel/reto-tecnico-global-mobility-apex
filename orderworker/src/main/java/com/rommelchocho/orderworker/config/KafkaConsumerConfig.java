package com.rommelchocho.orderworker.config;

import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.ConsumerFactory;

import lombok.AllArgsConstructor;
import reactor.kafka.receiver.ReceiverOptions;

@Configuration
@AllArgsConstructor
public class KafkaConsumerConfig {

    private final ConsumerFactory<String, String> kafkaConsumerFactory;

    @Bean
    public ReceiverOptions<String, String> kafkaReceiverOptions() {
        ReceiverOptions<String, String> options = ReceiverOptions.<String, String>create(
                kafkaConsumerFactory.getConfigurationProperties() // ¡Obtenemos las props del YAML!
        );
        return options.subscription(List.of("orders-topic"));
    }
}
