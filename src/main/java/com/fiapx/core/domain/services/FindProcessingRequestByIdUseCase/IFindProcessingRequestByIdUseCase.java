package com.fiapx.core.domain.services.FindProcessingRequestByIdUseCase;

import com.fiapx.core.domain.entities.ProcessingRequest;

import java.util.List;

public interface IFindProcessingRequestByIdUseCase {
    ProcessingRequest execute(String id);
}
