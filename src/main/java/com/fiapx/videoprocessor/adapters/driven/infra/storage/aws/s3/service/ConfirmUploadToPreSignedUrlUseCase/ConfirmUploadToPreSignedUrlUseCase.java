package com.fiapx.videoprocessor.adapters.driven.infra.storage.aws.s3.service.ConfirmUploadToPreSignedUrlUseCase;

import com.fiapx.videoprocessor.adapters.driven.infra.storage.aws.s3.service.GeneratePreSignedUrlUseCase.IGeneratePreSignedUrlUseCase;
import com.fiapx.videoprocessor.core.domain.entities.EProcessingStatus;
import com.fiapx.videoprocessor.core.domain.entities.PreSignedResponse;
import com.fiapx.videoprocessor.core.domain.entities.ProcessingRequest;
import com.fiapx.videoprocessor.core.domain.entities.RequestMessage;
import com.fiapx.videoprocessor.core.domain.repositories.IProcessingRequestRepository;
import com.fiapx.videoprocessor.core.domain.services.usecases.FindProcessingRequestByIdUseCase.IFindProcessingRequestByIdUseCase;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URL;

@Service
public class ConfirmUploadToPreSignedUrlUseCase implements IConfirmUploadToPreSignedUrlUseCase {
    @Autowired
    private IProcessingRequestRepository processingRequestRepository;

    @Autowired
    private IFindProcessingRequestByIdUseCase findProcessingRequestByIdUseCase;

    @Autowired
    private SqsTemplate sqsTemplate;

    @Value("${spring.cloud.aws.sqs.name}")
    public String queueName;

    public ProcessingRequest execute(String id, String token) {
        ProcessingRequest request = findProcessingRequestByIdUseCase.execute(id);

        if(EProcessingStatus.PRE_SIGNED.equals(request.getStatus())){
            request.setStatus(EProcessingStatus.IN_PROGRESS);
        }

        request = processingRequestRepository.save(request);

        RequestMessage requestMessage = new RequestMessage();
        requestMessage.setId(request.getId().toString());
        requestMessage.setToken(token);
        sqsTemplate.send(queueName, requestMessage);

        return request;
    }
}
