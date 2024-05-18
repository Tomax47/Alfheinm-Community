package com.alfheim.aflheim_community.exception.password;

public class PasswordActiveRecordExistException extends RuntimeException {
    public PasswordActiveRecordExistException(String message) {
        super(message);
    }

    public PasswordActiveRecordExistException(String message, Throwable cause) {
        super(message, cause);
    }
}
