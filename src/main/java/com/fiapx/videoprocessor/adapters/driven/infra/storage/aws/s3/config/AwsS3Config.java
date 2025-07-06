package com.fiapx.videoprocessor.adapters.driven.infra.storage.aws.s3.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.AwsSessionCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Configuration
public class AwsS3Config {

    @Value("${spring.application.storage}")
    private String storageType;

    @Value("${spring.cloud.aws.credentials.type}")
    private String credentialsType;

    @Value("${spring.cloud.aws.region.static}")
    private String region;

    @Value("${spring.cloud.aws.credentials.secret-key}")
    private String secretKey;

    @Value("${spring.cloud.aws.credentials.access-key}")
    private String accessKey;

    @Value("${spring.cloud.aws.credentials.session-token}")
    private String sessionToken;

    @Bean
    public S3Client s3Client() {
        AwsCredentials credentials = getCredentials();

        return S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .build();
    }

    @Bean
    public S3Presigner s3Presigner() {
        AwsCredentials credentials = getCredentials();

        return S3Presigner.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .build();
    }

    private AwsCredentials getCredentials(){
        return switch (EAwsCredentialsType.fromString(credentialsType)) {
            case BASIC -> AwsBasicCredentials.create(accessKey, secretKey);
            case SESSION -> AwsSessionCredentials.create(accessKey,secretKey,sessionToken);
        };
    }
}