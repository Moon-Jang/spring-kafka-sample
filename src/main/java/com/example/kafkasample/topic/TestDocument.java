package com.example.kafkasample.topic;

import com.querydsl.core.annotations.QueryEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Document
@QueryEntity
@Getter
@Setter
public class TestDocument {

    @Id
    private String id;

    private String test;

    @Builder
    private TestDocument(String test) {
        this.id = UUID.randomUUID()
                .toString()
                .replaceAll("-", "");
        this.test = test;
    }
}
