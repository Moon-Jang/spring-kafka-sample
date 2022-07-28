package com.example.kafkasample.producer;

import com.example.kafkasample.recorder.KafkaEventRecorder;
import com.example.kafkasample.topic.BaseTopic;
import com.example.kafkasample.topic.SampleTopic;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

@Service
public class SampleTopicProducer extends BaseProducer<SampleTopic> {

    public SampleTopicProducer(KafkaTemplate<String, BaseTopic> kafkaTemplate, KafkaEventRecorder kafkaEventRecorder) {
        super(kafkaTemplate, kafkaEventRecorder);
    }

    @Override
    protected void handleSuccess(SampleTopic topic, SendResult<String, BaseTopic> result) {
    }

    @Override
    protected void handleFailure(SampleTopic topic, Throwable throwable) {
    }

}
