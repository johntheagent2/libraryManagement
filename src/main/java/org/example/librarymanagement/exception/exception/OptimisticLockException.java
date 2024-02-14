package org.example.librarymanagement.exception.exception;

import lombok.Getter;

@Getter
public class OptimisticLockException extends RuntimeException {

    private final String errorCode;

    public OptimisticLockException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
