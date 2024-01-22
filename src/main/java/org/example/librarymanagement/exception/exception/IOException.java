package org.example.librarymanagement.exception.exception;

import lombok.Getter;

@Getter
public class IOException extends RuntimeException{

    private final String errorCode;

    public IOException(String errorCode, String message){
        super(message);
        this.errorCode = errorCode;
    }
}
