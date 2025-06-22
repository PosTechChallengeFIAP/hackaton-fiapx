package com.fiapx.videoprocessor.core.application.message;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
public class EMessageTypeTest {

    @Test
    void toString_shouldReturnCorrectName_forError() {
        assertEquals("Error", EMessageType.ERROR.toString());
    }

    @Test
    void toString_shouldReturnCorrectName_forSuccess() {
        assertEquals("Success", EMessageType.SUCCESS.toString());
    }
}
