package com.fiapx.core.domain.services.CreateProcessingRequestUseCase;


import com.fiapx.core.domain.entities.ProcessingRequest;
import com.fiapx.core.domain.repositories.IProcessingRequestRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;


import static org.mockito.Mockito.when;

@SpringBootTest
public class CreateProcessingRequestUseCaseTest {

    @MockitoBean
    private IProcessingRequestRepository processingRequestRepository;

    @Autowired
    CreateProcessingRequestUseCase createProcessingRequestUseCase;

    @Test
    void executeTest(){

        ProcessingRequest req = new ProcessingRequest();
        req.setInputFileName("file.mp4");
        req.setOutputFileName("file.zip");

        when(processingRequestRepository.save(req)).thenReturn(req);

        ProcessingRequest requestResult = createProcessingRequestUseCase.execute(req);

        Assertions.assertEquals(req, requestResult);
    }
}
