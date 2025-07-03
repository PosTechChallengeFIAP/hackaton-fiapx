package com.fiapx.videoprocessor.core.domain.services.hooks.queue;

import com.fiapx.videoprocessor.core.domain.entities.RequestMessage;

public interface IProcessingRequestQueueListener {
    void handleProcessingRequest(RequestMessage requestMessage);
}
