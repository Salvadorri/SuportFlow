package com.suportflow.backend.exception;

public class UniqueFieldAlreadyExistsException extends RuntimeException {

    public UniqueFieldAlreadyExistsException(String message) {
        super(message);
    }

    public UniqueFieldAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}