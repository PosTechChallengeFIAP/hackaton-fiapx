package com.fiapx.core.domain.services.SaveUploadedFileUseCase;

import com.fiapx.core.application.exceptions.UnableToSaveUploadedFileException;
import com.fiapx.core.domain.repositories.IProcessingRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Service
public class SaveUploadedFileUseCase implements ISaveUploadedFileUseCase {
    @Autowired
    private IProcessingRequestRepository processingRequestRepository;

    public void execute(String fileName, InputStream fileInputStream) {
        try {
            Files.copy(fileInputStream, Path.of("uploads/" + fileName), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new UnableToSaveUploadedFileException(fileName,e);
        }
    }
}
