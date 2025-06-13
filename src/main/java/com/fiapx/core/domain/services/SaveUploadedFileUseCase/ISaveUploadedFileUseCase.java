package com.fiapx.core.domain.services.SaveUploadedFileUseCase;

import java.io.InputStream;

public interface ISaveUploadedFileUseCase {
    void execute(String fileName, InputStream fileInputStream);
}
