package org.example.librarymanagement.exception.exception;

import lombok.Getter;

@Getter
public class MessageException extends RuntimeException{

    private final String errorCode;

    public MessageException(String message, String errorCode){
        super(message);
        this.errorCode = errorCode;
    }
}
