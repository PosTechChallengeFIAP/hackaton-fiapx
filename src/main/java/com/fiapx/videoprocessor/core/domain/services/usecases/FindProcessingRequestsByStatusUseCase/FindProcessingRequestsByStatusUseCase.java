package com.fiapx.videoprocessor.core.domain.services.usecases.FindProcessingRequestsByStatusUseCase;

import com.fiapx.videoprocessor.core.domain.entities.EProcessingStatus;
import com.fiapx.videoprocessor.core.domain.entities.ProcessingRequest;
import com.fiapx.videoprocessor.core.domain.repositories.IProcessingRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FindProcessingRequestsByStatusUseCase implements IFindProcessingRequestsByStatusUseCase {
    @Autowired
    private IProcessingRequestRepository processingRequestRepository;

    public List<ProcessingRequest> execute(EProcessingStatus status, String username) {
        return processingRequestRepository.findRequestByStatus(status.ordinal(), username);
    }
}
