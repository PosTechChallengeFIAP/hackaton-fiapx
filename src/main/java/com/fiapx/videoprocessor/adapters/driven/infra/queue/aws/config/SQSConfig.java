package com.fiapx.videoprocessor.adapters.driven.infra.queue.aws.config;

import com.fiapx.videoprocessor.adapters.driven.infra.storage.aws.s3.config.EAwsCredentialsType;
import io.awspring.cloud.sqs.config.SqsMessageListenerContainerFactory;
import io.awspring.cloud.sqs.listener.QueueNotFoundStrategy;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import io.awspring.cloud.sqs.support.converter.SqsMessagingMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.StringMessageConverter;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.AwsSessionCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

@Configuration
public class SQSConfig {

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
    public SqsAsyncClient sqsAsyncClient() {
        AwsCredentials credentials = switch (EAwsCredentialsType.fromString(credentialsType)) {
            case BASIC -> AwsBasicCredentials.create(accessKey, secretKey);
            case SESSION -> AwsSessionCredentials.create(accessKey,secretKey,sessionToken);
        };

        return SqsAsyncClient.builder()
                .region(Region.of(region)) // or your preferred region
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .build();
    }

    @Bean
    public SqsTemplate sqsTemplate(SqsAsyncClient sqsAsyncClient) {
        return SqsTemplate.builder()
                .sqsAsyncClient(sqsAsyncClient)
                .configure(c -> c.queueNotFoundStrategy(QueueNotFoundStrategy.FAIL))
                .build();
    }

}
