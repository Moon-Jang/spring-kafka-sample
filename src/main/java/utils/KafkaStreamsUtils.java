package utils;

import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.springframework.kafka.support.serializer.JsonSerializer;

public class KafkaStreamsUtils {

    private static final JsonSerializer jsonSerializer = new JsonSerializer<>();

    public static JsonSerde createSerde(Class c) {
        return new JsonSerde<>(jsonSerializer, new JsonDeserializer<>(c));
    }
}
