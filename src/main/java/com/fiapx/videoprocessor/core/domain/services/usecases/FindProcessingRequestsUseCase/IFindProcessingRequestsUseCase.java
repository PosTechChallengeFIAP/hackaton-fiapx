package com.fiapx.videoprocessor.core.domain.services.usecases.FindProcessingRequestsUseCase;

import com.fiapx.videoprocessor.core.domain.entities.ProcessingRequest;

import java.util.List;

public interface IFindProcessingRequestsUseCase {
    List<ProcessingRequest> execute();
}
