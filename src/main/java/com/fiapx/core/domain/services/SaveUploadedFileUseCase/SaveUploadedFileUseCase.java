package com.fiapx.core.domain.services.SaveUploadedFileUseCase;

import com.fiapx.core.application.exceptions.UnableToSaveUploadedFileException;
import com.fiapx.core.domain.repositories.IProcessingRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class SaveUploadedFileUseCase implements ISaveUploadedFileUseCase {
    @Autowired
    private IProcessingRequestRepository processingRequestRepository;

    @Value("${spring.application.upload-location}")
    private String uploadDir;

    public void execute(String fileName, InputStream fileInputStream) {
        try {
            Files.copy(fileInputStream, Paths.get(uploadDir,fileName), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new UnableToSaveUploadedFileException(fileName,e);
        }
    }
}
