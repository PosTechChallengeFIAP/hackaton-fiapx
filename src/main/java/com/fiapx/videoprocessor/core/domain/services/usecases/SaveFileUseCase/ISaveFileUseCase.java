package com.fiapx.videoprocessor.core.domain.services.usecases.SaveFileUseCase;

import java.io.InputStream;

public interface ISaveFileUseCase {
    void execute(String fileName, InputStream fileInputStream, String location, boolean skipLocal);
}
