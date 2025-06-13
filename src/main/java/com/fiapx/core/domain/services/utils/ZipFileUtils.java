package com.fiapx.core.domain.services.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipFileUtils {
        public static void createZip(List<Path> filePaths, Path zipPath) throws IOException {
            try (ZipOutputStream zipOut = new ZipOutputStream(Files.newOutputStream(zipPath))) {
                for (Path filePath : filePaths) {
                    File file = filePath.toFile();
                    if (file.isFile()) {
                        try (FileInputStream fis = new FileInputStream(file)) {
                            ZipEntry zipEntry = new ZipEntry(filePath.getFileName().toString());
                            zipOut.putNextEntry(zipEntry);

                            byte[] buffer = new byte[1024];
                            int length;
                            while ((length = fis.read(buffer)) > 0) {
                                zipOut.write(buffer, 0, length);
                            }

                            zipOut.closeEntry();
                        }
                    }
                }
            }
        }
    }
