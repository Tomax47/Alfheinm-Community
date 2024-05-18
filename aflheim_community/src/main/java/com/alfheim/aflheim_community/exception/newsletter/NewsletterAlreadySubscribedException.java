package com.alfheim.aflheim_community.exception.newsletter;

public class NewsletterAlreadySubscribedException extends RuntimeException {
    public NewsletterAlreadySubscribedException(String message) {
        super(message);
    }

    public NewsletterAlreadySubscribedException(String message, Throwable cause) {
        super(message, cause);
    }
}
