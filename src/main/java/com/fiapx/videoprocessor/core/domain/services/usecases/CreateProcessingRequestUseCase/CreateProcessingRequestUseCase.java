package com.fiapx.videoprocessor.core.domain.services.usecases.CreateProcessingRequestUseCase;

import com.fiapx.videoprocessor.core.domain.entities.ProcessingRequest;
import com.fiapx.videoprocessor.core.domain.entities.RequestMessage;
import com.fiapx.videoprocessor.core.domain.repositories.IProcessingRequestRepository;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CreateProcessingRequestUseCase implements ICreateProcessingRequestUseCase {
    @Autowired
    private IProcessingRequestRepository processingRequestRepository;

    @Autowired
    private SqsTemplate sqsTemplate;

    @Value("${spring.cloud.aws.sqs.name}")
    public String queueName;

    public ProcessingRequest execute(ProcessingRequest request) {
        request.validate();

        request = processingRequestRepository.save(request);

        RequestMessage requestMessage = new RequestMessage();
        requestMessage.setId(request.getId().toString());
        sqsTemplate.send(queueName, requestMessage);

        return request;
    }
}
