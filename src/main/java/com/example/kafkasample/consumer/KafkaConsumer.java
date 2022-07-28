package com.example.kafkasample.consumer;

import com.example.kafkasample.topic.PartitionTestTopic;
import com.example.kafkasample.topic.SampleTopic;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class KafkaConsumer {

    private final ObjectMapper objectMapper;

//    @KafkaListener(
//            topics = SampleTopic.TOPIC_NAME,
//            groupId = "test-1")
//    public void consume(ConsumerRecord<String, String> consumerRecord) throws IOException {
//        SampleTopic sampleTopic = objectMapper.convertValue(consumerRecord.value(), SampleTopic.class);
//        System.out.println(
//                String.format("Consumed message partition - (%d): %s",
//                        consumerRecord.partition(),
//                        consumerRecord.value()));
//    }
//
//    @KafkaListener(
//            topics = PartitionTestTopic.TOPIC_NAME,
//            groupId = "group-2")
//    public void consumePartitionTest(ConsumerRecord<String, String> consumerRecord, Acknowledgment ack) throws IOException {
//        PartitionTestTopic partitionTestTopic = objectMapper.convertValue(consumerRecord.value(), PartitionTestTopic.class);
//
//        System.out.println(
//                "offset:" + consumerRecord.offset() + "\n" +
//                        String.format("Consumed message partition - (%d): %s",
//                                consumerRecord.partition(),
//                                consumerRecord.value()));
//        //ack.acknowledge();
//    }

}
