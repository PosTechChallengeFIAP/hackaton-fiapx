package com.fiapx.videoprocessor.adapters.driven.infra.storage.aws.s3.config;

import com.fiapx.videoprocessor.adapters.driven.infra.storage.UnableToParseStorageType;
import lombok.Getter;

@Getter
public enum EAwsCredentialsType {
    BASIC("basic"),
    SESSION("session");

    private String description;

    EAwsCredentialsType(String description){
        this.description = description;
    }

    public static EAwsCredentialsType fromString(String credentialsType){
        for(EAwsCredentialsType value : EAwsCredentialsType.values()){
            if(value.getDescription().equalsIgnoreCase(credentialsType)){
                return value;
            }
        }

        throw new UnableToParseCredentialsType(credentialsType);
    }
}
