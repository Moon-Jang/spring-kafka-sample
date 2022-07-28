package com.example.kafkasample.streams;

import org.apache.kafka.streams.StreamsBuilder;

public interface KafkaStreamProcessor {

    public void process(StreamsBuilder streamBuilder);

}
