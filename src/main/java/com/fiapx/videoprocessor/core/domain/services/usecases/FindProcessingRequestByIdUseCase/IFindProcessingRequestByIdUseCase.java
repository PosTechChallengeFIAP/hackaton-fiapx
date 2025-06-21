package com.fiapx.videoprocessor.core.domain.services.usecases.FindProcessingRequestByIdUseCase;

import com.fiapx.videoprocessor.core.domain.entities.ProcessingRequest;

public interface IFindProcessingRequestByIdUseCase {
    ProcessingRequest execute(String id);
}
