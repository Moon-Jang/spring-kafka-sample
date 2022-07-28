package com.example.kafkasample.config;

import com.example.kafkasample.exception.KStreamExceptionHandler;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration;
import org.springframework.kafka.config.KafkaStreamsConfiguration;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafkaStreams
class KafkaStreamsConfig {

    private final String bootstrapAddress;
    private final String APPLICATION_ID;

    public KafkaStreamsConfig(@Value("${spring.kafka.bootstrap-servers}") String bootstrapAddress) throws UnknownHostException {
        this.bootstrapAddress = bootstrapAddress;
        this.APPLICATION_ID = "sample-stream-application-" + InetAddress.getLocalHost().getHostName();
    }

    @Primary
    @Bean(name = KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_CONFIG_BEAN_NAME)
    public KafkaStreamsConfiguration kStreamsConfig() {
        Map<String, Object> props = new HashMap<>();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, APPLICATION_ID);
        props.put(StreamsConfig.PROCESSING_GUARANTEE_CONFIG, "exactly_once");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        return new KafkaStreamsConfiguration(props);
    }

    @Bean
    public StreamsBuilder kStreamBuilder(@Qualifier(KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_BUILDER_BEAN_NAME) StreamsBuilderFactoryBean streamBuilderFactoryBean) throws Exception {
        streamBuilderFactoryBean.setStreamsUncaughtExceptionHandler(new KStreamExceptionHandler());

        return streamBuilderFactoryBean.getObject();
    }

}