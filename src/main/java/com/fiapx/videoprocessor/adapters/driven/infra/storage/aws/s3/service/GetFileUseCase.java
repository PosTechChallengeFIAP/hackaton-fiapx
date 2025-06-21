package com.fiapx.videoprocessor.adapters.driven.infra.storage.aws.s3.service;

import com.fiapx.videoprocessor.adapters.driven.infra.storage.aws.s3.UnableToReadFileFromAwsS3;
import com.fiapx.videoprocessor.core.domain.services.usecases.GetFileUseCase.IGetFileUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

import java.io.*;

@Service("awsGetFileUseCase")
public class GetFileUseCase implements IGetFileUseCase {
    @Autowired
    private S3Client s3Client;

    @Override
    public File execute(String location, String fileName) {
        GetObjectRequest request = GetObjectRequest.builder()
                .bucket(location)
                .key(fileName)
                .build();

        String filePath = String.format("tempDir/s3Temp/%s/%s", location, fileName);

        try (ResponseInputStream<GetObjectResponse> s3Object = s3Client.getObject(request);
             OutputStream outputStream = new FileOutputStream(filePath)) {

            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = s3Object.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            return new File(filePath);
        } catch (IOException e) {
            throw new UnableToReadFileFromAwsS3(fileName);
        }
    }
}
