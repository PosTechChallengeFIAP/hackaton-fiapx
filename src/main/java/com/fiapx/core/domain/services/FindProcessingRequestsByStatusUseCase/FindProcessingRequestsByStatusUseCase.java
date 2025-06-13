package com.fiapx.core.domain.services.FindProcessingRequestsByStatusUseCase;

import com.fiapx.core.domain.entities.EProcessingStatus;
import com.fiapx.core.domain.entities.ProcessingRequest;
import com.fiapx.core.domain.repositories.IProcessingRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FindProcessingRequestsByStatusUseCase implements IFindProcessingRequestsByStatusUseCase {
    @Autowired
    private IProcessingRequestRepository processingRequestRepository;

    public List<ProcessingRequest> execute(EProcessingStatus status) {
        return processingRequestRepository.findRequestByStatus(status.ordinal());
    }
}
