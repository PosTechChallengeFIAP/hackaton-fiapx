package com.fiapx.core.domain.services.FindProcessingRequestsUseCase;

import com.fiapx.core.domain.entities.ProcessingRequest;
import com.fiapx.core.domain.repositories.IProcessingRequestRepository;
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
