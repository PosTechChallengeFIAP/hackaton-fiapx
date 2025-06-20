package com.fiapx.videoprocessor.core.domain.repositories;

import com.fiapx.videoprocessor.core.domain.entities.ProcessingRequest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IProcessingRequestRepository {
    ProcessingRequest save(ProcessingRequest request);
    List<ProcessingRequest> findAll();
    List<ProcessingRequest> findRequestByStatus(int idStatus);
    Optional<ProcessingRequest> findById(UUID id);
    void delete(ProcessingRequest request);
}
