package com.fiapx.core.domain.services.FindProcessingRequestsByStatusUseCase;

import com.fiapx.core.application.exceptions.ValidationException;
import com.fiapx.core.domain.entities.EProcessingStatus;
import com.fiapx.core.domain.entities.ProcessingRequest;
import com.fiapx.core.domain.repositories.IProcessingRequestRepository;
import com.fiapx.core.domain.services.FindProcessingRequestByIdUseCase.FindProcessingRequestByIdUseCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.fiapx.core.domain.entities.EProcessingStatus.IN_PROGRESS;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
