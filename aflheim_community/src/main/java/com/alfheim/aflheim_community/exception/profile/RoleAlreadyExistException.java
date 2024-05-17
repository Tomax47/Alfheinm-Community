package com.alfheim.aflheim_community.exception.profile;

public class RoleAlreadyExistException extends RuntimeException {

    public RoleAlreadyExistException(String message) {
        super(message);
    }

    public RoleAlreadyExistException(String message, Throwable cause) {
        super(message, cause);
    }
}
