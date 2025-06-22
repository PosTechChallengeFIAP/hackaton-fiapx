package com.fiapx.videoprocessor.core.domain.services.utils;

import com.fiapx.videoprocessor.core.application.exceptions.DirectoryManagerException;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.*;
import java.nio.file.attribute.FileTime;
import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
public class DirectoryManagerTest {

    private static final Path TEST_DIR = Paths.get("test-dir");

    @BeforeEach
    void setUp() throws IOException {
        Files.createDirectories(TEST_DIR);
    }

    @AfterEach
    void tearDown() throws IOException {
        if (Files.exists(TEST_DIR)) {
            try (var paths = Files.walk(TEST_DIR)) {
                paths.sorted((a, b) -> b.compareTo(a))
                        .forEach(path -> {
                            try {
                                Files.deleteIfExists(path);
                            } catch (IOException ignored) {
                            }
                        });
            }
        }
    }

    @Test
    void createDir_shouldCreateDirectory() throws Exception {
        Path newDir = TEST_DIR.resolve("subdir");

        DirectoryManager.createDir(newDir);

        assertTrue(Files.exists(newDir));
        assertTrue(Files.isDirectory(newDir));
    }

    @Test
    void createDir_shouldThrowException_whenCannotCreateDirectory() throws Exception {
        Path filePath = TEST_DIR.resolve("someFile.txt");
        Files.createFile(filePath);

        DirectoryManagerException ex = assertThrows(
                DirectoryManagerException.class,
                () -> DirectoryManager.createDir(filePath)
        );

        assertTrue(ex.getMessage().contains("Unable to create directory"));
    }

    @Test
    void cleanUpDir_shouldDeleteFilesOlderThan30Days() throws Exception {
        Path oldFile = TEST_DIR.resolve("old.txt");
        Files.writeString(oldFile, "arquivo velho");
        Files.setLastModifiedTime(oldFile, FileTime.from(Instant.now().minusSeconds(31 * 24 * 60 * 60))); // 31 dias

        Path newFile = TEST_DIR.resolve("new.txt");
        Files.writeString(newFile, "arquivo novo");

        DirectoryManager.cleanUpDir(TEST_DIR);

        assertFalse(Files.exists(oldFile), "Arquivo antigo deve ser deletado");
        assertTrue(Files.exists(newFile), "Arquivo recente deve permanecer");
    }

    @Test
    void cleanUpDir_shouldThrowException_whenDirectoryIsInvalid() {
        Path invalidPath = TEST_DIR.resolve("nao-existe");

        DirectoryManagerException ex = assertThrows(
                DirectoryManagerException.class,
                () -> DirectoryManager.cleanUpDir(invalidPath)
        );

        assertTrue(ex.getMessage().contains("Unable to read directory"));
    }

    @Test
    void cleanUpDir_shouldHandleDeleteIOExceptionGracefully() throws IOException {
        Path filePath = TEST_DIR.resolve("undeletable.txt");
        Files.writeString(filePath, "arquivo para falha na exclusão");
        Files.setLastModifiedTime(filePath, FileTime.from(Instant.now().minusSeconds(31 * 24 * 60 * 60))); // 31 dias

        // Simula um arquivo "bloqueado" abrindo-o com canal não-fechado (em Windows impede exclusão)
        FileChannel lock = FileChannel.open(filePath, StandardOpenOption.WRITE);

        // Executa cleanup — não deve lançar exceção mesmo se não conseguir deletar
        assertDoesNotThrow(() -> DirectoryManager.cleanUpDir(TEST_DIR));

        // Cleanup: libera o lock e deleta o arquivo
        lock.close();
        Files.deleteIfExists(filePath);
    }

    @Test
    void cleanUpDir_shouldNotFail_whenGettingLastModifiedTimeThrows() throws IOException {
        // Arrange: cria um arquivo e depois o deleta antes de `isOlderThan()` ser chamado
        Path file = TEST_DIR.resolve("temp.txt");
        Files.writeString(file, "arquivo será apagado");

        // Usa um Thread para deletar o arquivo logo após a listagem começar
        new Thread(() -> {
            try {
                Thread.sleep(100); // pequena pausa para garantir que `Files.list()` já começou
                Files.deleteIfExists(file);
            } catch (Exception ignored) {}
        }).start();

        // Act + Assert: não deve lançar exceção mesmo com erro em getLastModifiedTime
        assertDoesNotThrow(() -> DirectoryManager.cleanUpDir(TEST_DIR));
    }
}
