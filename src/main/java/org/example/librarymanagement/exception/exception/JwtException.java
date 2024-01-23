package org.example.librarymanagement.exception.exception;

import lombok.Getter;

@Getter
public class JwtException extends RuntimeException{

    private final String errorCode;

    public JwtException(String errorCode, String message){
        super(message);
        this.errorCode = errorCode;
    }
}
