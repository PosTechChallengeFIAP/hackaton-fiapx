package com.fiapx.videoprocessor.core.domain.repositories;

import com.fiapx.videoprocessor.core.domain.entities.ProcessingRequest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IProcessingRequestRepository {
    ProcessingRequest save(ProcessingRequest request);
    List<ProcessingRequest> findAll(String username);
    List<ProcessingRequest> findRequestByStatus(int idStatus, String username);
    Optional<ProcessingRequest> findById(UUID id);
    void delete(ProcessingRequest request);
}
