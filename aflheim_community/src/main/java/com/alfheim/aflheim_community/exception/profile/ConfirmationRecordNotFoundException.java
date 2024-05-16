package com.alfheim.aflheim_community.exception.profile;

public class ConfirmationRecordNotFoundException extends RuntimeException {
    public ConfirmationRecordNotFoundException(String message) {
        super(message);
    }

    public ConfirmationRecordNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
