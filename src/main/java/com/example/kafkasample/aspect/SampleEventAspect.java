package com.example.kafkasample.aspect;

import com.example.kafkasample.dto.SendMessageRequest;
import com.example.kafkasample.producer.SampleTopicProducer;
import com.example.kafkasample.topic.SampleTopic;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Aspect
@Component
@RequiredArgsConstructor
public class SampleEventAspect {

    private final SampleTopicProducer producer;

    @After("" +
            "@annotation(com.example.kafkasample.aspect.annotation.SampleEvent) &&" +
            "args(.., @org.springframework.web.bind.annotation.RequestBody requestDto)")
    public void publishSampleEvent(SendMessageRequest requestDto) throws Throwable {
        LocalDateTime now = LocalDateTime.now();

        SampleTopic topic = SampleTopic.builder()
                .message(requestDto.getMessage() + now.getHour() + ":" + now.getMinute() + ":" + now.getSecond())
                .build();
        producer.publish(topic);
        //producer.publish();
    }
}
