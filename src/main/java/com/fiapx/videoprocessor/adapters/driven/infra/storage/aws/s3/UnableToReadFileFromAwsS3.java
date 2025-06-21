package com.fiapx.videoprocessor.adapters.driven.infra.storage.aws.s3;

public class UnableToReadFileFromAwsS3 extends RuntimeException {
    public UnableToReadFileFromAwsS3(String fileName){
        super(String.format("Unable to find file '%s' on s3 bucket.", fileName));
    }
}
