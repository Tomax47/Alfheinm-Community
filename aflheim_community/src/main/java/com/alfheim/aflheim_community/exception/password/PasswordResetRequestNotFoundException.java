package com.alfheim.aflheim_community.exception.password;

public class PasswordResetRequestNotFoundException extends RuntimeException {
    public PasswordResetRequestNotFoundException(String message) {
        super(message);
    }

    public PasswordResetRequestNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
