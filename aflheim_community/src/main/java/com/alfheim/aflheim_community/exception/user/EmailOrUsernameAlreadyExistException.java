package com.alfheim.aflheim_community.exception.user;

public class EmailOrUsernameAlreadyExistException extends RuntimeException {
    public EmailOrUsernameAlreadyExistException(String message) {
        super(message);
    }

    public EmailOrUsernameAlreadyExistException(String message, Throwable cause) {
        super(message, cause);
    }
}
