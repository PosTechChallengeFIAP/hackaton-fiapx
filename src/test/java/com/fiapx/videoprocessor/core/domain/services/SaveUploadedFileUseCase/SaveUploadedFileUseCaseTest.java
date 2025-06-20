package com.fiapx.videoprocessor.core.domain.services.SaveUploadedFileUseCase;

import com.fiapx.videoprocessor.core.application.exceptions.UnableToSaveUploadedFileException;
import com.fiapx.videoprocessor.core.domain.services.SaveUploadedFileUseCase.SaveUploadedFileUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class SaveUploadedFileUseCaseTest {

    @Autowired
    private SaveUploadedFileUseCase saveUploadedFileUseCase;

    private final String uploadDir = "test-uploads";

    @Test
    void testExecute_shouldSaveFileSuccessfully() throws Exception {

        ReflectionTestUtils.setField(saveUploadedFileUseCase, "uploadDir", uploadDir);
        String fileName = "testfile.txt";
        InputStream inputStream = new ByteArrayInputStream("conteúdo de teste".getBytes());

        Files.createDirectories(Paths.get(uploadDir));

        saveUploadedFileUseCase.execute(fileName, inputStream);

        Path savedPath = Paths.get(uploadDir, fileName);
        assertTrue(Files.exists(savedPath));

        Files.deleteIfExists(savedPath);
        Files.deleteIfExists(Paths.get(uploadDir));
    }

    @Test
    void testExecute_shouldThrowException_onIOException() {

        ReflectionTestUtils.setField(saveUploadedFileUseCase, "uploadDir", "/caminho/invalido/sem/permissao");
        String fileName = "fail.txt";
        InputStream inputStream = new ByteArrayInputStream("teste".getBytes());

        assertThrows(UnableToSaveUploadedFileException.class, () -> {
            saveUploadedFileUseCase.execute(fileName, inputStream);
        });
    }
}
