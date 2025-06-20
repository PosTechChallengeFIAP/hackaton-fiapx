package com.fiapx.videoprocessor.core.application.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class MessageResponse {
    private EMessageType messageType;
    private String message;

    public static MessageResponse type(EMessageType messageType){
        return new MessageResponse(messageType, null);
    }

    public MessageResponse withMessage(String message){
        this.setMessage(message);
        return this;
    }
}
