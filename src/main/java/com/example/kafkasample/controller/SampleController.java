package com.example.kafkasample.controller;

import com.example.kafkasample.aspect.annotation.SampleEvent;
import com.example.kafkasample.dto.SendMessageRequest;
import com.example.kafkasample.service.SampleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

@RestController
@RequiredArgsConstructor
public class SampleController {

    private final SampleService sampleService;

    @SampleEvent
    @PostMapping("/kafka-message")
    public String sendMessage(@RequestBody SendMessageRequest request) throws ExecutionException, InterruptedException {
        return sampleService.sendMessage(request);
    }

}
