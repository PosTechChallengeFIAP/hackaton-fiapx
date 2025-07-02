package com.fiapx.videoprocessor.adapters.driven.infra.storage.local.service;

import com.fiapx.videoprocessor.core.application.exceptions.UnableToSaveUploadedFileException;
import com.fiapx.videoprocessor.core.domain.services.usecases.SaveFileUseCase.ISaveFileUseCase;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service("localSaveFileUseCase")
public class SaveFileUseCase implements ISaveFileUseCase {
    public void execute(String fileName, InputStream fileInputStream, String location, boolean skipLocal) {
        if(skipLocal) return;

        try {
            Files.copy(fileInputStream, Paths.get(location,fileName), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new UnableToSaveUploadedFileException(fileName,e);
        }
    }
}
