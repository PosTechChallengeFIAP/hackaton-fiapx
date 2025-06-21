package com.fiapx.videoprocessor.adapters.driven.infra.storage.aws.s3.service.hooks.scheduled;

import com.fiapx.videoprocessor.adapters.driven.infra.storage.EStorageType;
import com.fiapx.videoprocessor.adapters.driven.infra.storage.aws.s3.constants.AwsDirectoryConstants;
import com.fiapx.videoprocessor.core.domain.services.hooks.scheduled.ITempFileRetentionService;
import com.fiapx.videoprocessor.core.domain.services.utils.DirectoryManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.nio.file.Path;

@Service
public class AwsTempFileRetentionService  implements ITempFileRetentionService {

    @Value("${spring.application.upload-location}")
    private String uploadDir;

    @Value("${spring.application.output-location}")
    private String outputDir;

    @Value("${spring.application.storage}")
    private String storageType;

    @Scheduled(cron = "0 0 1 * * *") // Runs daily at 1 AM
    public void cleanOldFiles() {
        if(EStorageType.fromString(storageType).equals(EStorageType.AWS)) {
            DirectoryManager.cleanUpDir(Path.of(String.format("%s/%s", AwsDirectoryConstants.S3_TEMP, uploadDir)));
            DirectoryManager.cleanUpDir(Path.of(String.format("%s/%s", AwsDirectoryConstants.S3_TEMP, outputDir)));
        }
    }
}