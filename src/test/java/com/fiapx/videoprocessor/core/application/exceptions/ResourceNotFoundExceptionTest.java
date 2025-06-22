package com.fiapx.videoprocessor.core.application.exceptions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
public class ResourceNotFoundExceptionTest {


    static class DummyClass {}

    @Test
    void constructor_withClass_shouldSetMessageCorrectly() {
        ResourceNotFoundException ex = new ResourceNotFoundException(DummyClass.class);
        assertEquals("DummyClass not found.", ex.getMessage());
    }

    @Test
    void constructor_withClassAndInfo_shouldSetMessageCorrectly() {
        ResourceNotFoundException ex = new ResourceNotFoundException(DummyClass.class, "ID 123");
        assertEquals("DummyClass not found. ID 123", ex.getMessage());
    }
}
