package com.alfheim.aflheim_community.exception.user;

public class UserPageNotFoundException extends RuntimeException {
    public UserPageNotFoundException(String message) {
        super(message);
    }

    public UserPageNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
