package com.example.kafkasample.topic;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@Document(collection = "second-branch-topic")
public class SecondBranchTopic extends BaseTopic {

    public final static String TOPIC_NAME = "second-branch-topic";

    private String message;
    @Builder
    private SecondBranchTopic(String message) {
        super(TOPIC_NAME);
        this.message = message;
    }

    public static SecondBranchTopic from(RootTopic rootTopic) {
        return new SecondBranchTopic(rootTopic.getMessage());
    }

}
