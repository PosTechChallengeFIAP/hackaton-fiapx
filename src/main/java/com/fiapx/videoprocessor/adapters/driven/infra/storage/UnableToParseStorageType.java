package com.fiapx.videoprocessor.adapters.driven.infra.storage;

public class UnableToParseStorageType extends RuntimeException {
    public UnableToParseStorageType(String storageType){
        super(String.format("Invalid Storage '%s'. Please check configuration."));
    }
}
