package com.fiapx.videoprocessor.core.domain.services.useCases.CreateProcessingRequestUseCase;

import com.fiapx.videoprocessor.adapters.driven.infra.queue.aws.service.SQSListener;
import com.fiapx.videoprocessor.core.domain.entities.ProcessingRequest;
import com.fiapx.videoprocessor.core.domain.repositories.IProcessingRequestRepository;
import com.fiapx.videoprocessor.core.domain.services.usecases.CreateProcessingRequestUseCase.CreateProcessingRequestUseCase;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

import java.util.UUID;

import static org.mockito.Mockito.when;

@SpringBootTest
public class CreateProcessingRequestUseCaseTest {

    @MockitoBean
    private IProcessingRequestRepository processingRequestRepository;

    @MockitoBean
    private SqsAsyncClient sqsAsyncClient;

    @MockitoBean
    private SqsTemplate sqsTemplate;

    @MockitoBean
    private SQSListener sqsListener;

    @Autowired
    CreateProcessingRequestUseCase createProcessingRequestUseCase;

    @Test
    void executeTest(){

        ProcessingRequest req = new ProcessingRequest();
        req.setId(UUID.randomUUID());
        req.setInputFileName("file.mp4");
        req.setOutputFileName("file.zip");

        when(processingRequestRepository.save(req)).thenReturn(req);

        ProcessingRequest requestResult = createProcessingRequestUseCase.execute(req);

        Assertions.assertEquals(req, requestResult);
    }
}
