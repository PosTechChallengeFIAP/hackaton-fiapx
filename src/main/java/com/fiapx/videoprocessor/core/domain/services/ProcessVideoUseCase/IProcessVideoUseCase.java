package com.fiapx.videoprocessor.core.domain.services.ProcessVideoUseCase;

import com.fiapx.videoprocessor.core.domain.entities.ProcessingRequest;

public interface IProcessVideoUseCase {
    void execute(ProcessingRequest request);
}
