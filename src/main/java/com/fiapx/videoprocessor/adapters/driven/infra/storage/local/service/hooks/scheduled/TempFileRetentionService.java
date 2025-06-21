package com.fiapx.videoprocessor.adapters.driven.infra.storage.local.service.hooks.scheduled;

import com.fiapx.videoprocessor.core.application.constants.DirectoryConstants;
import com.fiapx.videoprocessor.core.domain.services.hooks.scheduled.ITempFileRetentionService;
import com.fiapx.videoprocessor.core.domain.services.utils.DirectoryManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.nio.file.Path;

@Service
public class TempFileRetentionService implements ITempFileRetentionService {

    @Scheduled(cron = "0 0 1 * * *") // Runs daily at 1 AM
    public void cleanOldFiles() {
        DirectoryManager.cleanUpDir(Path.of(DirectoryConstants.PROCESSING_DIR));
    }
}