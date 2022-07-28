package com.example.kafkasample.topic;

import com.example.kafkasample.exception.SampleErrorCode;
import com.example.kafkasample.exception.SampleException;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.kafka.support.SendResult;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Getter
@Setter
@NoArgsConstructor
public abstract class BaseTopic {

    @Id
    private String id; // 메시지 고유 식별자 UUID

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    protected LocalDateTime publishedTime; // 메시지 발행시간

    @JsonIgnore
    @Transient
    private String topicName; // 토픽명

    @JsonIgnore
    protected List<KafkaConsumerGroup> consumerGroups; // 컨슈머 그룹

    @JsonIgnore
    protected Integer partition = -1; // 발행된 파티션

    @JsonIgnore
    protected Long offset = -1L; // 발행된 오프셋

    @JsonIgnore
    protected Boolean isPublished = false; // 메시지 정상 발행 여부

    @JsonIgnore
    protected int retryCount; // 재발행 횟수

    public BaseTopic(String topicName) {
        this.id = UUID.randomUUID()
                .toString()
                .replaceAll("-", "");
        this.topicName = topicName;
        this.publishedTime = LocalDateTime.now();
    }

    public void successPublish(int partition, long offset) {
        this.isPublished = true;
        this.publishedTime = LocalDateTime.now();
        this.partition = partition;
        this.offset = offset;
    }

    public void successPublish() {
        this.isPublished = true;
        this.publishedTime = LocalDateTime.now();
    }

    public void ackFromStreams() {

    }

    public void failPublish() {
        this.isPublished = false;
    }

    public void successRetry() {
        this.retryCount += 1;
    }

    public KafkaConsumerGroup getGroupByGroupId(String groupId) {
        if (consumerGroups == null) {
            return null;
        }

        return consumerGroups.stream()
                .filter(group -> group.getGroupId().equals(groupId))
                .findFirst()
                .orElseGet(() -> null);
    }

    public int getGroupIndexByGroupId(String groupId) {
        KafkaConsumerGroup group = getGroupByGroupId(groupId);

        if (group == null) {
            throw new SampleException(SampleErrorCode.DATA_NOT_FOUND);
        }

        return consumerGroups.indexOf(group);
    }

    public boolean isAck(String groupId) {
        KafkaConsumerGroup group = getGroupByGroupId(groupId);

        if (group == null) {
            return false;
        }

        return group.getIsAck();
    }
}
