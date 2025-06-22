package com.fiapx.videoprocessor.core.domain.entities;

import com.fiapx.videoprocessor.core.application.exceptions.ValidationException;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
public class ProcessingRequestTest {

    @Test
    void shouldThrowExceptionWhenInputFileNameIsNull() {
        ProcessingRequest request = new ProcessingRequest();
        request.setInputFileName(null);

        ValidationException ex = assertThrows(ValidationException.class, request::validate);
        assertEquals("Invalid file path", ex.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenInputFileNameIsEmpty() {
        ProcessingRequest request = new ProcessingRequest();
        request.setInputFileName("");

        ValidationException ex = assertThrows(ValidationException.class, request::validate);
        assertEquals("Invalid file path", ex.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenFileExtensionIsInvalid() {
        ProcessingRequest request = new ProcessingRequest();
        request.setInputFileName("video.txt");

        ValidationException ex = assertThrows(ValidationException.class, request::validate);
        assertEquals("Invalid file extension. Use: mp4, avi, mov, mkv", ex.getMessage());
    }

    @Test
    void shouldPassValidationWhenFileExtensionIsValid() {
        ProcessingRequest request = new ProcessingRequest();
        request.setInputFileName("video.mp4");

        assertDoesNotThrow(request::validate);
    }

    @Test
    void equalsShouldReturnTrueForSameId() {
        UUID id = UUID.randomUUID();

        ProcessingRequest r1 = new ProcessingRequest();
        r1.setId(id);

        ProcessingRequest r2 = new ProcessingRequest();
        r2.setId(id);

        assertEquals(r1, r2);
        assertEquals(r1.hashCode(), r2.hashCode());
    }

    @Test
    void equalsShouldReturnFalseForDifferentIds() {
        ProcessingRequest r1 = new ProcessingRequest();
        r1.setId(UUID.randomUUID());

        ProcessingRequest r2 = new ProcessingRequest();
        r2.setId(UUID.randomUUID());

        assertNotEquals(r1, r2);
    }

    @Test
    void equalsShouldReturnFalseForDifferentType() {
        ProcessingRequest r1 = new ProcessingRequest();
        r1.setId(UUID.randomUUID());

        String other = "Not a ProcessingRequest";

        assertNotEquals(r1, other);
    }

    @Test
    void equalsShouldReturnTrueForSameInstance() {
        ProcessingRequest r1 = new ProcessingRequest();
        r1.setId(UUID.randomUUID());

        assertEquals(r1, r1);
    }
}
