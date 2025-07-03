package com.fiapx.videoprocessor.core.domain.services.usecases.CreateProcessingRequestUseCase;

import com.fiapx.videoprocessor.core.domain.entities.ProcessingRequest;

public interface ICreateProcessingRequestUseCase {
    ProcessingRequest execute(ProcessingRequest request, String token);
}
