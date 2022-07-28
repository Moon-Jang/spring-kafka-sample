package com.example.kafkasample.topic;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@Document(collection = "third-branch-topic")
public class ThirdBranchTopic extends BaseTopic {

    public final static String TOPIC_NAME = "third-branch-topic";

    private String message;

    @Builder
    private ThirdBranchTopic(String message) {
        super(TOPIC_NAME);
        this.message = message;
    }

    public static ThirdBranchTopic from(RootTopic rootTopic) {
        return new ThirdBranchTopic(rootTopic.getMessage());
    }

}
