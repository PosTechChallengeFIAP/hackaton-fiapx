package com.fiapx.core.domain.services.ProcessVideoUseCase;

import com.fiapx.core.domain.entities.ProcessingRequest;

public interface IProcessVideoUseCase {
    void execute(ProcessingRequest request);
}
