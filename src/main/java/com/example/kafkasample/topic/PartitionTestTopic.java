package com.example.kafkasample.topic;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "partition-test-topic")
public class PartitionTestTopic extends BaseTopic {

    public final static String TOPIC_NAME = "partition-test-topic";

    private String message;

    public PartitionTestTopic() {
        super(TOPIC_NAME);
    }

    @Builder
    private PartitionTestTopic(String message) {
        super(TOPIC_NAME);
        this.message = message;
    }

}
