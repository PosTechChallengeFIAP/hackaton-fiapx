package com.fiapx.videoprocessor.adapters.driven.infra.storage.aws.s3.service.CreatePreSignedProcessingRequestUseCase;

import com.fiapx.videoprocessor.core.domain.entities.PreSignedResponse;
import com.fiapx.videoprocessor.core.domain.entities.ProcessingRequest;

public interface ICreatePreSignedProcessingRequestUseCase {
    PreSignedResponse execute(ProcessingRequest request, String location);
}
