package com.PetClinic.exception;

import java.util.UUID;

public class NotFoundException extends RuntimeException {

    public NotFoundException() {
        super("Resource not found");
    }

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundException(Throwable cause) {
        super(cause);
    }

    public NotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public NotFoundException(String entityType, UUID id) {
        super(entityType + " with id " + id + " not found");
    }

}