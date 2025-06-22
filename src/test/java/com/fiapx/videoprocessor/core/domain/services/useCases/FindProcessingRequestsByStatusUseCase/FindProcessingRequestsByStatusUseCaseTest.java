package com.fiapx.videoprocessor.core.domain.services.useCases.FindProcessingRequestsByStatusUseCase;

import com.fiapx.videoprocessor.core.domain.entities.EProcessingStatus;
import com.fiapx.videoprocessor.core.domain.entities.ProcessingRequest;
import com.fiapx.videoprocessor.core.domain.repositories.IProcessingRequestRepository;
import com.fiapx.videoprocessor.core.domain.services.usecases.FindProcessingRequestsByStatusUseCase.FindProcessingRequestsByStatusUseCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.ArrayList;
import java.util.List;

import static com.fiapx.videoprocessor.core.domain.entities.EProcessingStatus.IN_PROGRESS;
import static org.mockito.Mockito.when;

@SpringBootTest
public class FindProcessingRequestsByStatusUseCaseTest {

    @MockitoBean
    IProcessingRequestRepository processingRequestRepository;

    @Autowired
    FindProcessingRequestsByStatusUseCase findProcessingRequestsByStatusUseCase;

    @Test
    void executeTest(){

        ProcessingRequest req = new ProcessingRequest();
        req.setInputFileName("file.mp4");
        req.setOutputFileName("file.zip");

        List<ProcessingRequest> reqList = new ArrayList<>();
        reqList.add(req);
        EProcessingStatus status = IN_PROGRESS;

        when(processingRequestRepository.findRequestByStatus(status.ordinal()) ).thenReturn(reqList);

        List<ProcessingRequest> retrivedRequests = findProcessingRequestsByStatusUseCase.execute(status);

        Assertions.assertEquals(reqList, retrivedRequests);

    }

}
