package com.alfheim.aflheim_community.exception.stripe;

public class CardTokenGeneratingFailureException extends RuntimeException {
    public CardTokenGeneratingFailureException(String message) {
        super(message);
    }

    public CardTokenGeneratingFailureException(String message, Throwable cause) {
        super(message, cause);
    }
}
