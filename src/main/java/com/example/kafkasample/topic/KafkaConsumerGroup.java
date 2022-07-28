package com.example.kafkasample.topic;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class KafkaConsumerGroup {

    @JsonIgnore
    private String groupId; // 컨슈머 그룹 Id

    @JsonIgnore
    private Boolean isAck = false; // 메시지 정상 수신 여부

    protected LocalDateTime ackTime; // 메시지 수신 시간

    public KafkaConsumerGroup(String groupId) {
        this.groupId = groupId;
    }

}
