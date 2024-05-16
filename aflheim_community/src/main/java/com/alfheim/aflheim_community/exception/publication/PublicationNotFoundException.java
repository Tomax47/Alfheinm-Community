package com.alfheim.aflheim_community.exception.publication;

public class PublicationNotFoundException extends RuntimeException {

    public PublicationNotFoundException(String message) {
        super(message);
    }

    public PublicationNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
