package com.fiapx.videoprocessor.adapters.driven.infra.storage.aws.s3.service;

import com.fiapx.videoprocessor.core.domain.services.usecases.SaveFileUseCase.ISaveFileUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.InputStream;

@Service("awsSaveFileUseCase")
public class SaveFileUseCase implements ISaveFileUseCase {

    @Autowired
    private S3Client s3Client;

    public void execute(String fileName, InputStream fileInputStream, long contentLength,String location, boolean skipLocal) {
        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(location)
                .key(fileName)
                .build();

        s3Client.putObject(request, RequestBody.fromInputStream(fileInputStream, contentLength));
    }
}
