package org.example.librarymanagement.exception.exception;

import lombok.Getter;

@Getter
public class MessageException extends RuntimeException{

    private final String errorCode;

    public MessageException(String errorCode, String message){
        super(message);
        this.errorCode = errorCode;
    }
}
