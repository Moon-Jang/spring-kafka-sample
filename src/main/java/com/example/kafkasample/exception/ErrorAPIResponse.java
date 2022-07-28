package com.example.kafkasample.exception;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;
import java.util.stream.Collectors;

@Getter
@Slf4j
public class ErrorAPIResponse {
    private Map<String, Object> error = new LinkedHashMap<>();

    public ErrorAPIResponse(String message) {
        error.put("message", message);
    }

    public ErrorAPIResponse(SampleErrorCode errorCode) {
        error.put("code", errorCode.getCode());
        error.put("message", errorCode.getDescription());
    }

    public ErrorAPIResponse(SampleErrorCode errorCode, String message) {
        error.put("code", errorCode.getCode());
        error.put("message", message);
    }

    public ErrorAPIResponse(SampleErrorCode errorCode, Exception exception) {
        error.put("code", errorCode.getCode());
        error.put("message", errorCode.getDescription());
        error.put("detail", generateErrorStackTraceString(exception));
    }

    private String generateErrorStackTraceString(Exception exception) {
        StringWriter stringWriter = new StringWriter();
        exception.printStackTrace(new PrintWriter(stringWriter));
        log.error(stringWriter.toString());

        return Arrays.stream(stringWriter.toString().split("\r\n\tat"))
                .limit(10)
                .collect(Collectors.joining("\\n"));
    }

    @SuppressWarnings("serial")
    public ErrorAPIResponse(MethodArgumentNotValidException validationError) {
        List<Map<String,Object>> invalidParams = validationError
                .getBindingResult().getAllErrors().stream()
                .map( m -> (FieldError)m )
                .map( m -> new HashMap<String,Object>(){{
                    put("code", m.getCode());
                    put("message",m.getDefaultMessage());
                    put("field",m.getField());
                }}).collect(Collectors.toList());
        
        error.put("code", "INVALID PARAMETERS");
        error.put("message", "유효하지 않은 인자값 입니다.");
        error.put("invalidParameters", invalidParams);
    }

    @SuppressWarnings("serial")
    public ErrorAPIResponse(BindException bindingError) {
        List<Map<String,Object>> invalidParams = bindingError
                .getBindingResult().getAllErrors().stream()
                .map( m -> (FieldError)m )
                .map( m -> new HashMap<String,Object>(){{
                    put("code", m.getCode());
                    put("message",m.getDefaultMessage());
                    put("field",m.getField());
                }}).collect(Collectors.toList());
        
        error.put("code", "INVALID PARAMETERS");
        error.put("message", "인자값이 부족합니다.");
        error.put("invalidParameters", invalidParams);
    }
}
