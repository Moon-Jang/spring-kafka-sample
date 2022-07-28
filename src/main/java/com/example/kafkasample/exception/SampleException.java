package com.example.kafkasample.exception;

import lombok.Getter;
import org.springframework.http.ResponseEntity;

@Getter
public class SampleException extends RuntimeException {

    private SampleErrorCode errorCode;

    public SampleException(Exception e) {
        super(e);
    }

    public SampleException(SampleErrorCode errorCode) {
        super(errorCode.getDescription());
        this.errorCode = errorCode;
    }

    public static ResponseEntity<ErrorAPIResponse> toResponseEntity(final Exception e) {
        if (e instanceof SampleException) {
            SampleErrorCode errorCode = ((SampleException) e).getErrorCode();
            return new ResponseEntity<>(new ErrorAPIResponse(errorCode), errorCode.getHttpStatus());
        } else if (e.getCause() instanceof SampleException) {
            SampleErrorCode errorCode = ((SampleException) e).getErrorCode();
            return new ResponseEntity<>(new ErrorAPIResponse(errorCode), errorCode.getHttpStatus());
        } else {
            SampleErrorCode errorCode = SampleErrorCode.UNKNOWN_ERROR;
            return new ResponseEntity<>(new ErrorAPIResponse(errorCode, e), errorCode.getHttpStatus());
        }
    }

}