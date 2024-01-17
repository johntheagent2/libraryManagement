package org.example.librarymanagement.exception.exception;

import lombok.Getter;

@Getter
public class ExpiredJwtException extends RuntimeException{

    private final String errorCode;

    public ExpiredJwtException(String errorCode, String message){
        super(message);
        this.errorCode = errorCode;
    }
}
