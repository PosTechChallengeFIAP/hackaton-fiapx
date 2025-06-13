package com.fiapx.core.domain.services.FindProcessingRequestByIdUseCase;

import com.fiapx.core.application.exceptions.ResourceNotFoundException;
import com.fiapx.core.application.exceptions.ValidationException;
import com.fiapx.core.domain.entities.ProcessingRequest;
import com.fiapx.core.domain.repositories.IProcessingRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class FindProcessingRequestByIdUseCase implements IFindProcessingRequestByIdUseCase {
    @Autowired
    private IProcessingRequestRepository processingRequestRepository;

    public ProcessingRequest execute(String id) {
        try {
            UUID uuid = UUID.fromString(id);

            return processingRequestRepository.findById(uuid)
                    .orElseThrow(() -> new ResourceNotFoundException(ProcessingRequest.class));
        }catch (IllegalArgumentException ex){
            throw new ValidationException("Invalid ID. Cannot find resource.", ex);
        }
    }
}
