package com.fiapx.videoprocessor.core.domain.entities;
import com.fiapx.videoprocessor.core.application.exceptions.UnableToParseProcessingStatusException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
public class EProcessingStatusTest {

    @Test
    void fromValueShouldReturnIN_PROGRESS() {
        EProcessingStatus result = EProcessingStatus.IN_PROGRESS.fromValue("In Progress");
        assertEquals(EProcessingStatus.IN_PROGRESS, result);
    }

    @Test
    void fromValueShouldReturnCOMPLETED() {
        EProcessingStatus result = EProcessingStatus.IN_PROGRESS.fromValue("Completed");
        assertEquals(EProcessingStatus.COMPLETED, result);
    }

    @Test
    void fromValueShouldReturnERROR() {
        EProcessingStatus result = EProcessingStatus.IN_PROGRESS.fromValue("error");
        assertEquals(EProcessingStatus.ERROR, result);
    }

    @Test
    void fromValueShouldThrowExceptionForInvalidInput() {
        String invalid = "INVALID";

        UnableToParseProcessingStatusException exception = assertThrows(
                UnableToParseProcessingStatusException.class,
                () -> EProcessingStatus.IN_PROGRESS.fromValue(invalid)
        );

        assertTrue(exception.getMessage().contains(invalid));
    }
}
