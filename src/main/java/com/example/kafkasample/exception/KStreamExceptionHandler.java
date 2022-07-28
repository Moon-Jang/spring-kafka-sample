package com.example.kafkasample.exception;


import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.errors.StreamsUncaughtExceptionHandler;

@Slf4j
public class KStreamExceptionHandler implements StreamsUncaughtExceptionHandler {

    @Override
    public StreamThreadExceptionResponse handle(Throwable exception) {
        log.error("KStreamExceptionHandler error occur");
        exception.printStackTrace();
        return StreamThreadExceptionResponse.REPLACE_THREAD;
    }

}
