package com.fiapx.videoprocessor.adapters.driven.infra.storage.aws.s3.service.CreatePreSignedProcessingRequestUseCase;

import com.fiapx.videoprocessor.adapters.driven.infra.storage.aws.s3.config.EPreSignedUrlType;
import com.fiapx.videoprocessor.adapters.driven.infra.storage.aws.s3.service.GeneratePreSignedUrlUseCase.IGeneratePreSignedUrlUseCase;
import com.fiapx.videoprocessor.core.domain.entities.PreSignedResponse;
import com.fiapx.videoprocessor.core.domain.entities.ProcessingRequest;
import com.fiapx.videoprocessor.core.domain.repositories.IProcessingRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URL;

@Service
public class CreatePreSignedProcessingRequestUseCase implements ICreatePreSignedProcessingRequestUseCase {
    @Autowired
    private IProcessingRequestRepository processingRequestRepository;

    @Autowired
    private IGeneratePreSignedUrlUseCase generatePreSignedUrlUseCase;

    public PreSignedResponse execute(ProcessingRequest request, String location) {
        request.validate();

        request = processingRequestRepository.save(request);

        PreSignedResponse response = new PreSignedResponse();
        response.setId(request.getId().toString());

        URL preSignedUrl = generatePreSignedUrlUseCase.generatePreSignedUploadUrl(location, request.getInputFileName(), EPreSignedUrlType.UPLOAD);
        response.setPreSignedUrl(preSignedUrl.toString());

        return response;
    }
}
