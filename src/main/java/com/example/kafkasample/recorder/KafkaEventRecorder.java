package com.example.kafkasample.recorder;

import com.example.kafkasample.topic.BaseTopic;
import com.example.kafkasample.topic.KafkaConsumerGroup;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class KafkaEventRecorder {

    private final MongoTemplate mongoTemplate;

    public <T extends BaseTopic> boolean existsTopic(String topicId, Class<T> topicClass) {
        T topic = findTopicById(topicId, topicClass);
        return topic != null ? true : false;
    }

    public <T extends BaseTopic> void saveTopic(T topic) {
        Query query = new Query(Criteria.where("_id").is(topic.getId()));
        Document document = new Document();
        mongoTemplate.getConverter().write(topic, document);
        Update update = new Update().fromDocument(document);

        mongoTemplate.upsert(query, update, topic.getTopicName());
    }

    public <T extends BaseTopic> T findTopicById(String topicId, Class<T> topicClass) {
        return mongoTemplate.findById(topicId, topicClass);
    }

    public <T extends BaseTopic> void insertConsumerGroup(String topicId, String groupId,  Class<T> topicClass) {
        Query query = new Query(Criteria.where("_id").is(topicId));
        KafkaConsumerGroup group = new KafkaConsumerGroup(groupId);
        Update updateGroup = new Update()
                .push("consumerGroups", group);

        mongoTemplate.updateFirst(query, updateGroup, topicClass);
    }

    public <T extends BaseTopic>  void saveAcknowledgement(String topicId, String groupId,  Class<T> topicClass) {
        Query query = new Query(Criteria.where("_id").is(topicId));
        KafkaConsumerGroup group = new KafkaConsumerGroup(groupId);
        group.setIsAck(true);
        group.setAckTime(LocalDateTime.now());

        BaseTopic topic = (BaseTopic) mongoTemplate.findById(topicId, topicClass);
        final int index = topic.getGroupIndexByGroupId(groupId);
        Update updateGroup = new Update()
                .set("consumerGroups." + index, group);

        mongoTemplate.updateFirst(query, updateGroup, topicClass);
    }

    public <T extends BaseTopic> boolean existsConsumerGroup(String topicId, String groupId, Class<T> topicClass) {
        BaseTopic topic = mongoTemplate.findById(topicId, topicClass);
        KafkaConsumerGroup group = topic.getGroupByGroupId(groupId);

        return group != null ? true : false;
    }
}
