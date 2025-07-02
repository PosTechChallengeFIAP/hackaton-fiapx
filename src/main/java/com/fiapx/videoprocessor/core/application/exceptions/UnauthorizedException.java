package com.fiapx.videoprocessor.core.application.exceptions;

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message){
        super(message);
    }

    public UnauthorizedException(String message, Exception cause){
        super(message, cause);
    }
}
