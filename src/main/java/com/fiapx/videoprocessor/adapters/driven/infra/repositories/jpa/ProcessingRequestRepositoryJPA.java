package com.fiapx.videoprocessor.adapters.driven.infra.repositories.jpa;

import com.fiapx.videoprocessor.core.domain.entities.ProcessingRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface ProcessingRequestRepositoryJPA extends JpaRepository<ProcessingRequest, UUID> {
    @Query(value = "SELECT * FROM process WHERE status = ?1 AND username = ?2", nativeQuery = true)
    List<ProcessingRequest> findRequestByStatus(int statusId, String username);

    @Query(value = "SELECT * FROM process WHERE username = ?1", nativeQuery = true)
    List<ProcessingRequest> findAll(String username);
}
