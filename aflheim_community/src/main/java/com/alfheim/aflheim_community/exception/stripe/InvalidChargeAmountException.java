package com.alfheim.aflheim_community.exception.stripe;

public class InvalidChargeAmountException extends RuntimeException {
    public InvalidChargeAmountException(String message) {
        super(message);
    }

    public InvalidChargeAmountException(String message, Throwable cause) {
        super(message, cause);
    }
}
