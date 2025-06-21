package com.fiapx.videoprocessor.core.domain.services.utils;

import com.fiapx.videoprocessor.core.application.exceptions.DirectoryManagerException;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.FileTime;
import java.time.Duration;
import java.time.Instant;
import java.util.stream.Stream;

@Slf4j
public class DirectoryManager {
    public static void createDir(String dirPath) throws DirectoryManagerException {
        Path dir = Paths.get(dirPath);

        createDir(dir);
    }

    public static void createDir(Path dir) throws DirectoryManagerException {
        try {
            Files.createDirectories(dir);
        } catch (IOException ex) {
            throw new DirectoryManagerException(String.format("Unable to create directory '%s'. ", dir.getFileName()) +
                    ex.getMessage());
        }
    }

    public static void cleanUpDir(Path path){
        try (Stream<Path> files = Files.list(path)) {
            files.filter(Files::isRegularFile)
                    .filter(filePath -> isOlderThan(filePath, Duration.ofDays(30)))
                    .forEach(filePath -> {
                        try {
                            Files.delete(filePath);
                        } catch (IOException e) {
                            log.atError().log(String.format("Unable to delete file '%s'.\n%s",
                                    filePath.toString(), e.getMessage()));
                        }
                    });
        } catch (IOException e) {
            throw new DirectoryManagerException(String.format("Unable to read directory '%s'.\n%s ",
                    path, e.getMessage()));
        }
    }

    private static boolean isOlderThan(Path path, Duration duration) {
        FileTime lastModified = null;
        try {
            lastModified = Files.getLastModifiedTime(path);
            return lastModified.toInstant().isBefore(Instant.now().minus(duration));
        } catch (IOException e) {
            return true;
        }
    }
}