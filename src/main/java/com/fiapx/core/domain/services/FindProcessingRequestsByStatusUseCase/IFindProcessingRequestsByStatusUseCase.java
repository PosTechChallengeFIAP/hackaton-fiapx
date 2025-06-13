package com.fiapx.core.domain.services.FindProcessingRequestsByStatusUseCase;

import com.fiapx.core.domain.entities.EProcessingStatus;
import com.fiapx.core.domain.entities.ProcessingRequest;

import java.util.List;

public interface IFindProcessingRequestsByStatusUseCase {
    List<ProcessingRequest> execute(EProcessingStatus status);
}
