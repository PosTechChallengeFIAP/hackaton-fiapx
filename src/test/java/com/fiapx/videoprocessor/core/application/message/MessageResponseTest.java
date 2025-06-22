package com.fiapx.videoprocessor.core.application.message;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
public class MessageResponseTest {


    @Test
    void constructor_shouldSetFieldsCorrectly() {
        MessageResponse response = new MessageResponse(EMessageType.SUCCESS, "Operation completed");
        assertEquals(EMessageType.SUCCESS, response.getMessageType());
        assertEquals("Operation completed", response.getMessage());
    }

    @Test
    void type_shouldCreateInstanceWithNullMessage() {
        MessageResponse response = MessageResponse.type(EMessageType.ERROR);
        assertEquals(EMessageType.ERROR, response.getMessageType());
        assertNull(response.getMessage());
    }

    @Test
    void withMessage_shouldSetMessageAndReturnSameInstance() {
        MessageResponse response = MessageResponse.type(EMessageType.SUCCESS);
        MessageResponse result = response.withMessage("All good");
        assertEquals("All good", response.getMessage());
        assertSame(response, result);
    }
}
