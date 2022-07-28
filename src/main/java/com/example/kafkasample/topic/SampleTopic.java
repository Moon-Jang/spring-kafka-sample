package com.example.kafkasample.topic;

import com.querydsl.core.annotations.QueryEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@QueryEntity
@Document(collection = "sample-topic")
public class SampleTopic extends BaseTopic {

    public final static String TOPIC_NAME = "sample-test";

    private String message;

    public SampleTopic() {
        super(TOPIC_NAME);
    }

    @Builder
    private SampleTopic(String message) {
        super(TOPIC_NAME);
        this.message = message;
    }

}
