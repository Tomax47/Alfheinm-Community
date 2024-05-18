package com.alfheim.aflheim_community.exception.password;

public class PasswordResetRequestExpiredException extends RuntimeException {
    public PasswordResetRequestExpiredException(String message) {
        super(message);
    }

    public PasswordResetRequestExpiredException(String message, Throwable cause) {
        super(message, cause);
    }
}
