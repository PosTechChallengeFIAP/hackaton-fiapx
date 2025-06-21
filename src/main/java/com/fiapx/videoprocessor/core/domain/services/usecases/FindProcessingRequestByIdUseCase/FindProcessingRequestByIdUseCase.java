package com.fiapx.videoprocessor.core.domain.services.usecases.FindProcessingRequestByIdUseCase;

import com.fiapx.videoprocessor.core.application.exceptions.ResourceNotFoundException;
import com.fiapx.videoprocessor.core.application.exceptions.ValidationException;
import com.fiapx.videoprocessor.core.domain.entities.ProcessingRequest;
import com.fiapx.videoprocessor.core.domain.repositories.IProcessingRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
