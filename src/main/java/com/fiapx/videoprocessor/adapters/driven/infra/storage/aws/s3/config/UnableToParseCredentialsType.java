package com.fiapx.videoprocessor.adapters.driven.infra.storage.aws.s3.config;

public class UnableToParseCredentialsType extends RuntimeException {
    public UnableToParseCredentialsType(String credentialsType){
        super(String.format("Invalid AWS Credentials '%s'. Please check configuration."));
    }
}
