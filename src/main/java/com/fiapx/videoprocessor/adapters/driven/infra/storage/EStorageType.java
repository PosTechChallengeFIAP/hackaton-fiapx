package com.fiapx.videoprocessor.adapters.driven.infra.storage;

import lombok.Getter;

@Getter
public enum EStorageType {
    AWS("aws"),
    LOCAL("local");

    private String description;

    EStorageType(String description){
        this.description = description;
    }

    public static EStorageType fromString(String storageType){
        for(EStorageType value : EStorageType.values()){
            if(value.getDescription().equalsIgnoreCase(storageType)){
                return value;
            }
        }

        throw new UnableToParseStorageType(storageType);
    }
}
