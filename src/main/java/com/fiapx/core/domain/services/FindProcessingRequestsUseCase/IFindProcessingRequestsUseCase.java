package com.fiapx.core.domain.services.FindProcessingRequestsUseCase;

import com.fiapx.core.domain.entities.ProcessingRequest;

import java.util.List;

public interface IFindProcessingRequestsUseCase {
    List<ProcessingRequest> execute();
}
