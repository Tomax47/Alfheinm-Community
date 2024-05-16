package com.alfheim.aflheim_community.exception.publication;

public class PublicationPageNotFoundException extends RuntimeException {

    public PublicationPageNotFoundException(String msg) {
        super(msg);
    }

    public PublicationPageNotFoundException(String msg, Exception e) {
        super(msg, e);
    }
}
