package com.example.kafkasample.streams;

import com.example.kafkasample.recorder.KafkaEventRecorder;
import com.example.kafkasample.topic.*;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.ForeachAction;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import utils.KafkaStreamsUtils;

@Component
@RequiredArgsConstructor
public class SampleProcessor implements KafkaStreamProcessor {

    private final KafkaEventRecorder kafkaEventRecorder;

    @Override
    @Autowired
    public void process(StreamsBuilder kStreamBuilder) {
        // 1. 해당 토픽을 수집
        KStream<String, RootTopic> stream = kStreamBuilder
                .stream(RootTopic.TOPIC_NAME, Consumed.with(Serdes.String(),
                        KafkaStreamsUtils.createSerde(RootTopic.class)));

        // 2. 토픽 분기 처리
        KStream<String, RootTopic>[] branches = stream.branch(
                (key, value) -> value.getMessage().startsWith("(1)"),
                (key, value) -> value.getMessage().startsWith("(2)"),
                (key, value) -> value.getMessage().startsWith("(3)")
        );

        // 3-A . 1번 루트 분기 설정
        branches[0].mapValues(FirstBranchTopic::from)
                .peek(saveLog(kafkaEventRecorder))
                .to(FirstBranchTopic.TOPIC_NAME,
                    Produced.with(Serdes.String(), KafkaStreamsUtils.createSerde(FirstBranchTopic.class)));

        // 3-B . 2번 루트 분기 설정
        branches[1].mapValues(SecondBranchTopic::from)
                .peek(saveLog(kafkaEventRecorder))
                .to(SecondBranchTopic.TOPIC_NAME,
                    Produced.with(Serdes.String(), KafkaStreamsUtils.createSerde(SecondBranchTopic.class)));
        // 3-C . 3번 루트 분기 설정
        branches[2].mapValues(ThirdBranchTopic::from)
                .peek(saveLog(kafkaEventRecorder))
                .to(ThirdBranchTopic.TOPIC_NAME,
                    Produced.with(Serdes.String(), KafkaStreamsUtils.createSerde(ThirdBranchTopic.class)));

    }

    private ForeachAction<String, BaseTopic> saveLog(KafkaEventRecorder kafkaEventRecorder) {
        return (key, value) -> {
            value.successPublish();
            kafkaEventRecorder.saveTopic(value);
        };
    }


}
