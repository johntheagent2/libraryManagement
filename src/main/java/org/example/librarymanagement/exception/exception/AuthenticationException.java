package org.example.librarymanagement.exception.exception;

import lombok.Builder;
import lombok.Getter;

@Getter
public class AuthenticationException extends RuntimeException{

    private final String errorCode;

    public AuthenticationException(String message, String errorCode){
        super(message);
        this.errorCode = errorCode;
    }
}
