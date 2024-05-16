package com.alfheim.aflheim_community.exception.blacklist_record;

public class UserBlacklistRecordNotFoundException extends RuntimeException {
    public UserBlacklistRecordNotFoundException(String message) {
        super(message);
    }

    public UserBlacklistRecordNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
