package com.example.kafkasample.topic;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@Document(collection = "root-topic")
public class RootTopic extends BaseTopic {

    public final static String TOPIC_NAME = "root-topic";

    private String message;

    @Builder
    private RootTopic(String message) {
        super(TOPIC_NAME);
        this.message = message;
    }

}
