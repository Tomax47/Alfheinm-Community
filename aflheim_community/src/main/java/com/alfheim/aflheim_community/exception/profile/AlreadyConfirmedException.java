package com.alfheim.aflheim_community.exception.profile;

public class AlreadyConfirmedException extends RuntimeException {
    public AlreadyConfirmedException(String message) {
        super(message);
    }

    public AlreadyConfirmedException(String message, Throwable cause) {
        super(message, cause);
    }
}
