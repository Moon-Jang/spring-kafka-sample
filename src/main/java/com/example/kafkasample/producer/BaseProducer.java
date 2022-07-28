package com.example.kafkasample.producer;

import com.example.kafkasample.recorder.KafkaEventRecorder;
import com.example.kafkasample.topic.BaseTopic;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Slf4j
@RequiredArgsConstructor
public abstract class BaseProducer<T extends BaseTopic> {

    private final KafkaTemplate<String, BaseTopic> kafkaTemplate;
    private final int MAX_RETRY_COUNT = 3;
    private final KafkaEventRecorder kafkaEventRecorder;

    @Async
    public void publish(T topic) {
        if (!kafkaEventRecorder.existsTopic(topic.getId(), topic.getClass())) {
            kafkaEventRecorder.saveTopic(topic);
        }

        ListenableFuture<SendResult<String, BaseTopic>> future = kafkaTemplate.send(topic.getTopicName(), topic);
        future.addCallback(callback(topic));
    }

    /* 지정된 partition 이 있을 경우 */
    @Async
    public void publish(T topic, Integer partition) {
        ListenableFuture<SendResult<String, BaseTopic>> future = kafkaTemplate.send(topic.getTopicName(), partition, null, topic);
        future.addCallback(callback(topic, partition));
    }

    private ListenableFutureCallback<SendResult<String, BaseTopic>> callback(T topic) {
        return new ListenableFutureCallback<SendResult<String, BaseTopic>>() {
            @Override
            public void onSuccess(SendResult<String, BaseTopic> result) {
                handleSuccess(topic, result);
                saveSuccessTopic(topic, result);
            }

            @Override
            public void onFailure(final Throwable throwable) {
                handleFailure(topic, throwable);
                final int retryCount = saveFailTopic(topic);

                if (retryCount < MAX_RETRY_COUNT) {
                    publish(topic); // retry
                }
            }
        };
    }

    /* 지정된 partition 이 있을 경우 */
    private ListenableFutureCallback<SendResult<String, BaseTopic>> callback(T topic, Integer partition) {
        return new ListenableFutureCallback<SendResult<String, BaseTopic>>() {
            @Override
            public void onSuccess(SendResult<String, BaseTopic> result) {
                handleSuccess(topic, result);
                saveSuccessTopic(topic, result);
            }

            @Override
            public void onFailure(final Throwable throwable) {
                log.error("Kafka Publish Fail: ", throwable);
                handleFailure(topic, throwable);
                final int retryCount = saveFailTopic(topic);
                if (retryCount > MAX_RETRY_COUNT) {
                    /* TODO
                     *   3번 실패시 전략 결정
                     *   1. 주기적으로 실패한 메시지를 DB에서 조회해 재전송
                     *   2. 실패 큐를 지정해 해당 큐에 매시지 발행
                     *      (메시지 발행에 3번 실패했다는 것은 클러스터에 문제가 발생했다고 판단
                     *       다른 큐를 이용한다 EX) AWS-SQS, 다른 MQ )
                     * */
                    return;
                }

                publish(topic, partition); // retry
            }
        };
    }

    private void saveSuccessTopic(T topic, SendResult<String, BaseTopic> result) {
        final int partition = result.getRecordMetadata().partition();
        final long offset = result.getRecordMetadata().offset();
        topic.successPublish(partition, offset);

        try {
            if (kafkaEventRecorder.existsTopic(topic.getId(), topic.getClass())) {
                kafkaEventRecorder.saveTopic(topic);
                return;
            }

            kafkaEventRecorder.saveTopic(topic);
        } catch (Exception e) {
            log.error("MongoDB Error Occurs: ", e);
        }
    }

    private int saveFailTopic(T topic) {
        topic.failPublish();

        int retryCount = -1;

        try {
            T foundTopic = (T) kafkaEventRecorder.findTopicById(topic.getId(), topic.getClass());

            if (foundTopic != null) {
                if (foundTopic.getRetryCount() > 2) return foundTopic.getRetryCount();
                foundTopic.successRetry();
                kafkaEventRecorder.saveTopic(foundTopic);
                retryCount = foundTopic.getRetryCount();
            } else {
                topic.successRetry();
                kafkaEventRecorder.saveTopic(topic);
                retryCount = topic.getRetryCount();
            }
        } catch (Exception e) {
            log.error("MongoDB Error Occurs: ", e);
            retryCount = 99999;
        } finally {
            return retryCount;
        }
    }

    protected abstract void handleSuccess(T topic, SendResult<String, BaseTopic> result);

    protected abstract void handleFailure(T topic, final Throwable throwable);

}
