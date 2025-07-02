package com.fiapx.videoprocessor.adapters.driven.infra.repositories.impl;

import com.fiapx.videoprocessor.adapters.driven.infra.repositories.jpa.ProcessingRequestRepositoryJPA;
import com.fiapx.videoprocessor.core.domain.entities.ProcessingRequest;
import com.fiapx.videoprocessor.core.domain.repositories.IProcessingRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class ProcessingRequestRepository implements IProcessingRequestRepository {
    @Autowired
    ProcessingRequestRepositoryJPA processingRequestRepositoryJPA;

    @Override
    public ProcessingRequest save(ProcessingRequest requestToSave) {
        return processingRequestRepositoryJPA.save(requestToSave);
    }

    @Override
    public List<ProcessingRequest> findAll(String username) {
        return processingRequestRepositoryJPA.findAll(username);
    }

    @Override
    public Optional<ProcessingRequest> findById(UUID id) {
        return processingRequestRepositoryJPA.findById(id);
    }

    @Override
    public List<ProcessingRequest> findRequestByStatus(int idStatus, String username) {
        return processingRequestRepositoryJPA.findRequestByStatus(idStatus, username);
    }

    @Override
    public void delete(ProcessingRequest request) {
        processingRequestRepositoryJPA.delete(request);
    }
}
