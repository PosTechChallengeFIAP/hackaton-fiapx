package com.fiapx.core.application.exceptions;

public class UnableToParseProcessingStatusException extends RuntimeException {

    public UnableToParseProcessingStatusException(String value){
        super("Unable to parse request status. value = " + value);
    }
}
