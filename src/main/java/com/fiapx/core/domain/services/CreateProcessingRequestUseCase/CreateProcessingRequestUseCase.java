package com.fiapx.core.domain.services.CreateProcessingRequestUseCase;

import com.fiapx.core.domain.entities.ProcessingRequest;
import com.fiapx.core.domain.repositories.IProcessingRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateProcessingRequestUseCase implements ICreateProcessingRequestUseCase {
    @Autowired
    private IProcessingRequestRepository processingRequestRepository;

    public ProcessingRequest execute(ProcessingRequest request) {
        request.validate();

        return processingRequestRepository.save(request);
    }
}
