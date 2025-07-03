package com.fiapx.videoprocessor.core.application.exceptions;

import java.util.UUID;

public class RequestIsAlreadyProcessed extends RuntimeException {
    public RequestIsAlreadyProcessed(UUID id){
        super(String.format("Request '%s' is already completed. Try downloading the results.", id.toString()));
    }

}
