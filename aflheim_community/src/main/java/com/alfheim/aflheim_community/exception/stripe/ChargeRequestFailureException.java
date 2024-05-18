package com.alfheim.aflheim_community.exception.stripe;

public class ChargeRequestFailureException extends RuntimeException {
    public ChargeRequestFailureException(String message) {
        super(message);
    }

    public ChargeRequestFailureException(String message, Throwable cause) {
        super(message, cause);
    }
}
