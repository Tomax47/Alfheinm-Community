package com.alfheim.aflheim_community.exception.blacklist_record;

public class UserBlacklistActiveRecordException extends RuntimeException {
    public UserBlacklistActiveRecordException(String message) {
        super(message);
    }

    public UserBlacklistActiveRecordException(String message, Throwable cause) {
        super(message, cause);
    }
}
