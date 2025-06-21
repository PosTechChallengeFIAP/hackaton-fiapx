package com.fiapx.videoprocessor.core.domain.services.usecases.FindProcessingRequestsUseCase;

import com.fiapx.videoprocessor.core.domain.entities.ProcessingRequest;
import com.fiapx.videoprocessor.core.domain.repositories.IProcessingRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FindProcessingRequestsUseCase implements IFindProcessingRequestsUseCase {
    @Autowired
    private IProcessingRequestRepository processingRequestRepository;

    public List<ProcessingRequest> execute() {
        return processingRequestRepository.findAll();
    }
}
