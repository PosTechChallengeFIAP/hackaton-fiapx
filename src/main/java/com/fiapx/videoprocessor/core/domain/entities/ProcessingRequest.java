package com.fiapx.videoprocessor.core.domain.entities;

import com.fiapx.videoprocessor.core.application.exceptions.ValidationException;
import com.fiapx.videoprocessor.core.domain.services.utils.CheckFileExtension;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.sql.Timestamp;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "process")
public class ProcessingRequest {
    @Id
    @UuidGenerator
    private UUID id;

    @Column(nullable = false)
    private String inputFileName;

    private String outputFileName;

    @Column(nullable = false)
    private EProcessingStatus status;

    @Column(nullable = false)
    private Timestamp createdAt;

    @Column
    private Timestamp completedAt;

    @Column
    private String errorMessage;

    @Column
    private String username;

    public void validate() throws ValidationException {
        if (Objects.isNull(this.inputFileName) || this.inputFileName.isEmpty()) {
            throw new ValidationException("Invalid file path");
        }

        if(!CheckFileExtension.hasValidExtension(this.inputFileName)){
            throw new ValidationException("Invalid file extension. Use: mp4, avi, mov, mkv");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProcessingRequest)) return false;
        return id != null && id.equals(((ProcessingRequest) o).getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
