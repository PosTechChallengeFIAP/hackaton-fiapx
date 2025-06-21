package com.fiapx.videoprocessor.core.domain.services.usecases.ProcessVideoUseCase;

import com.fiapx.videoprocessor.core.domain.entities.ProcessingRequest;

public interface IProcessVideoUseCase {
    ProcessingRequest execute(ProcessingRequest request);
}
