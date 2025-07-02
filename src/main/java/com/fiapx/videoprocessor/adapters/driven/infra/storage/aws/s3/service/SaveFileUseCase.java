package com.fiapx.videoprocessor.adapters.driven.infra.storage.aws.s3.service;

import com.fiapx.videoprocessor.adapters.driven.infra.storage.aws.s3.constants.AwsDirectoryConstants;
import com.fiapx.videoprocessor.core.application.exceptions.UnableToSaveUploadedFileException;
import com.fiapx.videoprocessor.core.domain.services.usecases.SaveFileUseCase.ISaveFileUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Service("awsSaveFileUseCase")
public class SaveFileUseCase implements ISaveFileUseCase {

    @Autowired
    private S3Client s3Client;

    public void execute(String fileName, InputStream fileInputStream,String location, boolean skipLocal) {
        try {
            Path tempFile = Path.of(String.format("%s/%s/%s", AwsDirectoryConstants.S3_TEMP, location, fileName));
            Files.copy(fileInputStream, tempFile, StandardCopyOption.REPLACE_EXISTING);

            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(location)
                    .key(fileName)
                    .build();

            s3Client.putObject(request, RequestBody.fromFile(tempFile));

            Files.delete(tempFile);
        } catch (IOException e) {
            throw new UnableToSaveUploadedFileException(fileName, e);
        }
    }
}
