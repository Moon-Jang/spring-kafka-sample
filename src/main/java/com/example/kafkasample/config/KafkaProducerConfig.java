package com.example.kafkasample.config;

import com.example.kafkasample.topic.BaseTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
class KafkaProducerConfig {

    private final String bootstrapAddress;

    public KafkaProducerConfig(@Value("${spring.kafka.bootstrap-servers}") String bootstrapAddress) {
        this.bootstrapAddress = bootstrapAddress;
    }

    @Bean
    public KafkaTemplate<String, BaseTopic> kafkaTemplate() {
        Map<String, Object> producerConfigs = new HashMap<>();
        producerConfigs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        producerConfigs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        producerConfigs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        producerConfigs.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, 10000);
        producerConfigs.put(ProducerConfig.ACKS_CONFIG, "1");
        producerConfigs.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, 10000);
        DefaultKafkaProducerFactory factory = new DefaultKafkaProducerFactory<String, BaseTopic>(producerConfigs);

        return new KafkaTemplate<String, BaseTopic>(factory);
    }

}