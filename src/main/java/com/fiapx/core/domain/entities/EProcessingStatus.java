package com.fiapx.core.domain.entities;

import com.fiapx.core.application.exceptions.UnableToParseProcessingStatusException;
import lombok.Getter;

@Getter
public enum EProcessingStatus {
    IN_PROGRESS("In Progress"),
    COMPLETED("Completed"),
    ERROR("error");

    private String value;

    EProcessingStatus(String value) {
        this.value = value;
    }

    public EProcessingStatus fromValue(String value){
        for(EProcessingStatus category : EProcessingStatus.values()){
            if(category.value.equals(value)) return category;
        }

        throw new UnableToParseProcessingStatusException(value);
    }
}
