package com.fiapx.videoprocessor.core.domain.services.utils;

import com.fiapx.videoprocessor.adapters.driven.infra.queue.aws.service.SQSListener;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

@SpringBootTest
public class CheckFileExtensionTest {

    @MockitoBean
    private SqsAsyncClient sqsAsyncClient;

    @MockitoBean
    private SqsTemplate sqsTemplate;

    @MockitoBean
    private SQSListener sqsListener;

    @Test
    void executeTrueTest(){
        String fileName = "Arquivo.mp4";

        Assertions.assertEquals(true, CheckFileExtension.hasValidExtension(fileName));
    }

    @Test
    void executeFalseTest(){
        String fileName = "Arquivo.y";

        Assertions.assertEquals(false, CheckFileExtension.hasValidExtension(fileName));
    }
}
