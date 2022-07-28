package com.example.kafkasample.topic;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@Document(collection = "first-branch-topic")
public class FirstBranchTopic extends BaseTopic {

    public final static String TOPIC_NAME = "first-branch-topic";

    private String message;

    @Builder
    private FirstBranchTopic(String message) {
        super(TOPIC_NAME);
        this.message = message;
    }

    public static FirstBranchTopic from(RootTopic rootTopic) {
        return new FirstBranchTopic(rootTopic.getMessage());
    }

}
