package com.fiapx.videoprocessor.core.domain.services.utils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static org.junit.jupiter.api.Assertions.*;

public class ZipFileUtilsTest {

    private static final Path TEMP_DIR = Paths.get("temp-test-zip");
    private static final Path ZIP_PATH = TEMP_DIR.resolve("output.zip");
    private static final Path FILE1 = TEMP_DIR.resolve("file1.txt");
    private static final Path FILE2 = TEMP_DIR.resolve("file2.txt");

    @AfterEach
    void cleanup() throws IOException {
        Files.deleteIfExists(FILE1);
        Files.deleteIfExists(FILE2);
        Files.deleteIfExists(ZIP_PATH);
        Files.deleteIfExists(TEMP_DIR);
    }

    @Test
    void createZip_shouldCreateZipWithFiles() throws IOException {
        // Arrange
        Files.createDirectories(TEMP_DIR);
        Files.writeString(FILE1, "Conteúdo do arquivo 1");
        Files.writeString(FILE2, "Conteúdo do arquivo 2");

        List<Path> files = List.of(FILE1, FILE2);

        // Act
        ZipFileUtils.createZip(files, ZIP_PATH);

        // Assert
        assertTrue(Files.exists(ZIP_PATH), "Arquivo zip deve ter sido criado");

        // Verifica se os arquivos estão dentro do zip
        try (ZipInputStream zis = new ZipInputStream(Files.newInputStream(ZIP_PATH))) {
            ZipEntry entry1 = zis.getNextEntry();
            assertNotNull(entry1);
            assertTrue(entry1.getName().equals("file1.txt") || entry1.getName().equals("file2.txt"));

            ZipEntry entry2 = zis.getNextEntry();
            assertNotNull(entry2);
            assertTrue(entry2.getName().equals("file1.txt") || entry2.getName().equals("file2.txt"));

            assertNull(zis.getNextEntry(), "O zip deve conter exatamente 2 arquivos");
        }
    }
}
