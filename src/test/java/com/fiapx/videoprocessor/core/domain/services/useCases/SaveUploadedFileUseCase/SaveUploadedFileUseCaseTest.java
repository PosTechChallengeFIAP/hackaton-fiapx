package com.fiapx.videoprocessor.core.domain.services.SaveUploadedFileUseCase;

import com.fiapx.videoprocessor.core.application.exceptions.UnableToSaveUploadedFileException;
import com.fiapx.videoprocessor.core.domain.services.usecases.SaveFileUseCase.ISaveFileUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class SaveUploadedFileUseCaseTest {

    @Autowired
    private ISaveFileUseCase saveFileUseCase;

    private final String uploadDir = "test-uploads";

    @Test
    void testExecute_shouldSaveFileSuccessfully() throws Exception {

        ReflectionTestUtils.setField(saveFileUseCase, "uploadDir", uploadDir);
        String fileName = "testfile.txt";
        InputStream inputStream = new ByteArrayInputStream("conteúdo de teste".getBytes());

        Files.createDirectories(Paths.get(uploadDir));

        saveFileUseCase.execute(fileName, inputStream, uploadDir, false);

        Path savedPath = Paths.get(uploadDir, fileName);
        assertTrue(Files.exists(savedPath));

        Files.deleteIfExists(savedPath);
        Files.deleteIfExists(Paths.get(uploadDir));
    }

    @Test
    void testExecute_shouldThrowException_onIOException() {

        ReflectionTestUtils.setField(saveFileUseCase, "uploadDir", "/caminho/invalido/sem/permissao");
        String fileName = "fail.txt";
        InputStream inputStream = new ByteArrayInputStream("teste".getBytes());

        assertThrows(UnableToSaveUploadedFileException.class, () -> {
            saveFileUseCase.execute(fileName, inputStream, uploadDir, false);
        });
    }
}
