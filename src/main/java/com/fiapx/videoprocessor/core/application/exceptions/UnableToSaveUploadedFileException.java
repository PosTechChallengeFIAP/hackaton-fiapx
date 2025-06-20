package com.fiapx.videoprocessor.core.application.exceptions;

public class UnableToSaveUploadedFileException extends RuntimeException {
    public UnableToSaveUploadedFileException(String fileName, Exception cause) {
        super(String.format("Uploaded file '%s' was not saved.", fileName), cause);
    }
}
