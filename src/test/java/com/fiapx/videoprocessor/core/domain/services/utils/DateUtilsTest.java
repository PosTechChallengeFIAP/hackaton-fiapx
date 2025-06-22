package com.fiapx.videoprocessor.core.domain.services.utils;

import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
public class DateUtilsTest {

    @Test
    void timestampToString_shouldFormatCorrectly() {
        // Arrange
        LocalDateTime ldt = LocalDateTime.of(2023, 12, 25, 15, 30, 45, 123_000_000); // 123ms
        Timestamp timestamp = Timestamp.valueOf(ldt);

        // Act
        String result = DateUtils.timestampToString(timestamp);

        // Assert
        assertEquals("20231225153045123", result);
    }
}
