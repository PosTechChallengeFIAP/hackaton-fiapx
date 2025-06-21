package com.fiapx.videoprocessor.core.domain.services.usecases.GetFileUseCase;

import com.fiapx.videoprocessor.core.domain.entities.ProcessingRequest;

import java.io.File;

public interface IGetFileUseCase {
    File execute(String location, String fileName);
}
