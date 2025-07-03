package com.fiapx.videoprocessor.core.domain.services.useCases.FindProcessingRequestsUseCase;

import com.fiapx.videoprocessor.core.domain.entities.ProcessingRequest;
import com.fiapx.videoprocessor.core.domain.repositories.IProcessingRequestRepository;
import com.fiapx.videoprocessor.core.domain.services.usecases.FindProcessingRequestsUseCase.FindProcessingRequestsUseCase;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

@SpringBootTest
public class FindProcessingRequestsUseCaseTest {

    @MockitoBean
    IProcessingRequestRepository processingRequestRepository;

    @MockitoBean
    private SqsAsyncClient sqsAsyncClient;

    @MockitoBean
    private SqsTemplate sqsTemplate;

    @Autowired
    FindProcessingRequestsUseCase findProcessingRequestsUseCase;

    @Test
    void executeFindAllTest(){

        ProcessingRequest req = new ProcessingRequest();
        req.setInputFileName("file.mp4");
        req.setOutputFileName("file.zip");
        req.setUsername("user-1");

        List<ProcessingRequest> reqList = new ArrayList<>();
        reqList.add(req);

        when(processingRequestRepository.findAll("user-1")).thenReturn(reqList);

        Assertions.assertEquals(reqList,findProcessingRequestsUseCase.execute("user-1"));
    }

}
