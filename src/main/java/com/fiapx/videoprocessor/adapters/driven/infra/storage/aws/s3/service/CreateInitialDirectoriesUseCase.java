package com.fiapx.videoprocessor.adapters.driven.infra.storage.aws.s3.service;

import com.fiapx.videoprocessor.adapters.driven.infra.storage.aws.s3.constants.AwsDirectoryConstants;
import com.fiapx.videoprocessor.core.application.constants.DirectoryConstants;
import com.fiapx.videoprocessor.core.domain.services.usecases.CreateInitialDirectoriesUseCase.ICreateInitialDirectoriesUseCase;
import com.fiapx.videoprocessor.core.domain.services.utils.DirectoryManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service("awsCreateInitialDirectoriesUseCase")
public class CreateInitialDirectoriesUseCase implements ICreateInitialDirectoriesUseCase {

    @Value("${spring.application.upload-location}")
    private String uploadDir;

    @Value("${spring.application.output-location}")
    private String outputDir;

    @Override
    public void execute() {
        DirectoryManager.createDir(uploadDir);
        DirectoryManager.createDir(outputDir);
        DirectoryManager.createDir(DirectoryConstants.TEMP_DIR);
        DirectoryManager.createDir(DirectoryConstants.PROCESSING_DIR);
        DirectoryManager.createDir(AwsDirectoryConstants.S3_TEMP);
        DirectoryManager.createDir(String.format("%s/%s", AwsDirectoryConstants.S3_TEMP, uploadDir));
        DirectoryManager.createDir(String.format("%s/%s", AwsDirectoryConstants.S3_TEMP, outputDir));
    }
}
