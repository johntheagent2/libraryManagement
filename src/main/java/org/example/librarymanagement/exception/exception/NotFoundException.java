package org.example.librarymanagement.exception.exception;

import lombok.Getter;

@Getter
public class NotFoundException extends RuntimeException {

    private final String errorCode;

    public NotFoundException(String message, String errorCode){
        super(message);
        this.errorCode = errorCode;
    }
}
