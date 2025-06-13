package com.fiapx.core.domain.services.CreateProcessingRequestUseCase;

import com.fiapx.core.domain.entities.ProcessingRequest;

public interface ICreateProcessingRequestUseCase {
    ProcessingRequest execute(ProcessingRequest request);
}
