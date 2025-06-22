package com.fiapx.videoprocessor.core.domain.services.useCases.FindProcessingRequestByIdUseCase;

import com.fiapx.videoprocessor.core.application.exceptions.ValidationException;
import com.fiapx.videoprocessor.core.domain.entities.ProcessingRequest;
import com.fiapx.videoprocessor.core.domain.repositories.IProcessingRequestRepository;
import com.fiapx.videoprocessor.core.domain.services.usecases.FindProcessingRequestByIdUseCase.FindProcessingRequestByIdUseCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest
public class FindProcessingRequestByIdUseCaseTest {

    @MockitoBean
    IProcessingRequestRepository processingRequestRepository;

    @Autowired
    FindProcessingRequestByIdUseCase findProcessingRequestByIdUseCase;

    @Test
    void executeTest(){

        ProcessingRequest req = new ProcessingRequest();
        req.setInputFileName("file.mp4");
        req.setOutputFileName("file.zip");

        UUID reqId = UUID.randomUUID();
        when(processingRequestRepository.findById(reqId) ).thenReturn(Optional.of(req));

        ProcessingRequest retrivedRequest = findProcessingRequestByIdUseCase.execute(reqId.toString());

        Assertions.assertEquals(req, retrivedRequest);

    }

    @Test
    void executeExceptionTest(){

        ProcessingRequest req = new ProcessingRequest();
        req.setInputFileName("file.mp4");
        req.setOutputFileName("file.zip");

        String reqId = "idinvalido";

        ValidationException  ex = assertThrows(ValidationException .class,()->findProcessingRequestByIdUseCase.execute(reqId));

        Assertions.assertEquals("Invalid ID. Cannot find resource.", ex.getMessage());

    }

}
