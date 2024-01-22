package org.example.librarymanagement.exception.exception;

import lombok.Getter;

@Getter
public class TemplateException extends RuntimeException{

    private final String errorCode;

    public TemplateException(String errorCode, String message){
        super(message);
        this.errorCode = errorCode;
    }
}
