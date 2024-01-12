package org.example.librarymanagement.exception.exception;

import lombok.Builder;
import lombok.Getter;

@Getter
public class BadRequestException extends RuntimeException{

    private final String errorCode;

    public BadRequestException(String errorCode, String message){
        super(message);
        this.errorCode = errorCode;
    }
}
