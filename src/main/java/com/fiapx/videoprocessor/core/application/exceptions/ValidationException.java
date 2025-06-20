package com.fiapx.videoprocessor.core.application.exceptions;

public class ValidationException extends RuntimeException {
    public ValidationException(String message){
        super(message);
    }

    public ValidationException(String message, Exception cause){
        super(message, cause);
    }
}
