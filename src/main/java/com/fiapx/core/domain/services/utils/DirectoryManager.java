package com.fiapx.core.domain.services.utils;

import com.fiapx.core.application.exceptions.DirectoryManagerException;

import java.io.IOException;
import java.nio.file.*;

public class DirectoryManager {
    public static void recreateDir(String dirPath) throws DirectoryManagerException {
        Path dir = Paths.get(dirPath);

        // Delete directory if it exists
        if (Files.exists(dir)) {
            try {
                Files.walk(dir)
                        .sorted((a, b) -> b.compareTo(a)) // Reverse order to delete files first
                        .forEach(path -> {
                            try {
                                Files.delete(path);
                            } catch (Exception e) {
                                throw new DirectoryManagerException("Unable to delete temp directory. " + e.getMessage());
                            }
                        });
            }catch (IOException ex){
                throw new DirectoryManagerException("Unable to read existing temp directory. " + ex.getMessage());
            }
        }

        // Recreate directory
        try{
            Files.createDirectories(dir);
        }catch (IOException ex){
            throw new DirectoryManagerException("Unable to recreate temp directory. " + ex.getMessage());
        }
    }
}