package com.example.kafkasample.service;

import com.example.kafkasample.dto.SendMessageRequest;
import com.example.kafkasample.exception.SampleErrorCode;
import com.example.kafkasample.exception.SampleException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SampleService {

    public String sendMessage(SendMessageRequest request) {
        return "success";
    }

    public void doSomething() {
        throw new SampleException(SampleErrorCode.UNKNOWN_ERROR);
    }

}
