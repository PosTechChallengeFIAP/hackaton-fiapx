package com.fiapx.videoprocessor.adapters.driven.infra.queue.aws.service;

import com.fiapx.videoprocessor.core.domain.entities.ProcessingRequest;
import com.fiapx.videoprocessor.core.domain.entities.RequestMessage;
import com.fiapx.videoprocessor.core.domain.services.hooks.queue.IProcessingRequestQueueListener;
import com.fiapx.videoprocessor.core.domain.services.usecases.FindProcessingRequestByIdUseCase.IFindProcessingRequestByIdUseCase;
import com.fiapx.videoprocessor.core.domain.services.usecases.ProcessVideoUseCase.IProcessVideoUseCase;
import io.awspring.cloud.sqs.annotation.SqsListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SQSListener implements IProcessingRequestQueueListener {

    @Autowired
    private IProcessVideoUseCase processVideoUseCase;

    @Autowired
    private IFindProcessingRequestByIdUseCase findProcessingRequestByIdUseCase;

    @Override
    @SqsListener("${spring.cloud.aws.sqs.name}")
    public void handleProcessingRequest(RequestMessage requestMessage) {
        ProcessingRequest request = findProcessingRequestByIdUseCase.execute(requestMessage.getId());
        processVideoUseCase.execute(request);
    }
}
