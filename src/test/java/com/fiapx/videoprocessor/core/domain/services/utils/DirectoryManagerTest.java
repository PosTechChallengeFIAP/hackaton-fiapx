package com.fiapx.videoprocessor.core.domain.services.utils;

import com.fiapx.videoprocessor.core.application.exceptions.DirectoryManagerException;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;
public class DirectoryManagerTest {

    private static final String TEST_DIR = "test-temp-dir";

    @Test
    void createDir_shouldCreateDirectory() throws Exception {
        // Act
        DirectoryManager.createDir(TEST_DIR);

        // Assert
        Path dirPath = Path.of(TEST_DIR);
        assertTrue(Files.exists(dirPath));
        assertTrue(Files.isDirectory(dirPath));

        // Cleanup
        Files.deleteIfExists(dirPath);
    }

    @Test
    void recreateDir_shouldDeleteAndRecreateDirectory() throws Exception {
        Path dirPath = Path.of(TEST_DIR);
        Files.createDirectories(dirPath);
        Files.createFile(dirPath.resolve("temp.txt")); // cria um arquivo dentro

        // Act
        DirectoryManager.recreateDir(TEST_DIR);

        // Assert
        assertTrue(Files.exists(dirPath));
        assertTrue(Files.isDirectory(dirPath));
        assertEquals(0, Files.list(dirPath).count(), "Diretório deve estar vazio após recriação");

        // Cleanup
        Files.deleteIfExists(dirPath);
    }

    @Test
    void createDir_shouldThrowException_whenCannotCreateDirectory() throws Exception {
        // Cria um arquivo em vez de diretório
        Path filePath = Path.of("some-file.txt");
        Files.createFile(filePath);

        // Tenta criar um diretório com o mesmo nome do arquivo
        DirectoryManagerException exception = assertThrows(
                DirectoryManagerException.class,
                () -> DirectoryManager.createDir("some-file.txt")
        );

        assertTrue(exception.getMessage().contains("Unable to create directory"));

        // Cleanup
        Files.deleteIfExists(filePath);
    }

    @Test
    void recreateDir_shouldThrowException_whenWalkFails() throws IOException {
        String testPath = "error-dir";
        Path mockPath = Path.of(testPath);

        try (MockedStatic<Files> mockedFiles = org.mockito.Mockito.mockStatic(Files.class)) {
            mockedFiles.when(() -> Files.exists(mockPath)).thenReturn(true);
            mockedFiles.when(() -> Files.walk(mockPath)).thenThrow(new IOException("walk failed"));

            DirectoryManagerException exception = assertThrows(
                    DirectoryManagerException.class,
                    () -> DirectoryManager.recreateDir(testPath)
            );

            assertTrue(exception.getMessage().contains("Unable to read existing temp directory"));
        }
    }
}
