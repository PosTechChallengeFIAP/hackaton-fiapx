package com.fiapx.videoprocessor.adapters.driven.infra.storage.aws.s3.service.ConfirmUploadToPreSignedUrlUseCase;

import com.fiapx.videoprocessor.core.domain.entities.ProcessingRequest;

public interface IConfirmUploadToPreSignedUrlUseCase {
    ProcessingRequest execute(String id, String token);
}
