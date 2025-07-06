package com.fiapx.videoprocessor.adapters.driven.infra.storage.aws.s3.service.GeneratePreSignedUrlUseCase;

import com.fiapx.videoprocessor.adapters.driven.infra.storage.aws.s3.config.EPreSignedUrlType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.net.URL;
import java.time.Duration;

@Service
public class GeneratePreSignedUrlUseCase implements IGeneratePreSignedUrlUseCase {

    @Autowired
    private S3Presigner s3Presigner;

    public URL generatePreSignedUploadUrl(String bucket, String key, EPreSignedUrlType preSignedUrlType) {
        return switch (preSignedUrlType){
            case UPLOAD -> getUploadUrl(bucket, key);
            case DOWNLOAD -> getDownloadUrl(bucket, key);
        };
    }

    private URL getUploadUrl(String bucket, String key){
        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();

        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(15))
                .putObjectRequest(objectRequest)
                .build();

        return s3Presigner.presignPutObject(presignRequest).url();
    }

    private URL getDownloadUrl(String bucket, String key){
        var getObjectRequest = GetObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(15))
                .getObjectRequest(getObjectRequest)
                .build();

        return s3Presigner.presignGetObject(presignRequest).url();
    }

}
