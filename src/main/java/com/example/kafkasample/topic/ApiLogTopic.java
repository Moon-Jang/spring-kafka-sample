package com.example.kafkasample.topic;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "log-event-topic")
public class ApiLogTopic extends BaseTopic {

    public final static String TOPIC_NAME = "log_event";
    private String log;

    @Builder
    private ApiLogTopic(String log) {
        super(TOPIC_NAME);
        this.log = log;
    }

}
