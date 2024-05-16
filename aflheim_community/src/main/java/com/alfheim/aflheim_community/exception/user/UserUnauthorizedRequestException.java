package com.alfheim.aflheim_community.exception.user;

public class UserUnauthorizedRequestException extends RuntimeException {
    public UserUnauthorizedRequestException(String message) {
        super(message);
    }

    public UserUnauthorizedRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
