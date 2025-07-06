package com.fiapx.videoprocessor.adapters.driven.infra.storage.aws.s3.service.GeneratePreSignedUrlUseCase;

import java.net.URL;

public interface IGeneratePreSignedUrlUseCase {
    URL generatePreSignedUploadUrl(String bucket, String key);
}
