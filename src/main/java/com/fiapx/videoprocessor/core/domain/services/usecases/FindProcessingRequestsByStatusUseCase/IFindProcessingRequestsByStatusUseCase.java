package com.fiapx.videoprocessor.core.domain.services.usecases.FindProcessingRequestsByStatusUseCase;

import com.fiapx.videoprocessor.core.domain.entities.EProcessingStatus;
import com.fiapx.videoprocessor.core.domain.entities.ProcessingRequest;

import java.util.List;

public interface IFindProcessingRequestsByStatusUseCase {
    List<ProcessingRequest> execute(EProcessingStatus status, String username);
}
