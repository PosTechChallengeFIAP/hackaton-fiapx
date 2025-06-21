package com.fiapx.videoprocessor.core.domain.services.FindProcessingRequestsUseCase;

import com.fiapx.videoprocessor.core.domain.entities.ProcessingRequest;
import com.fiapx.videoprocessor.core.domain.repositories.IProcessingRequestRepository;
import com.fiapx.videoprocessor.core.domain.services.usecases.FindProcessingRequestsUseCase.FindProcessingRequestsUseCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

@SpringBootTest
public class FindProcessingRequestsUseCaseTest {

    @MockitoBean
    IProcessingRequestRepository processingRequestRepository;
    @Autowired
    FindProcessingRequestsUseCase findProcessingRequestsUseCase;

    @Test
    void executeFindAllTest(){

        ProcessingRequest req = new ProcessingRequest();
        req.setInputFileName("file.mp4");
        req.setOutputFileName("file.zip");

        List<ProcessingRequest> reqList = new ArrayList<>();
        reqList.add(req);

        when(processingRequestRepository.findAll()).thenReturn(reqList);

        Assertions.assertEquals(reqList,findProcessingRequestsUseCase.execute());
    }

}
