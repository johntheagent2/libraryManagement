package org.example.librarymanagement.exception.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiExceptionResponse {
    private String fieldName;
    private String message;
    private String messageCode;

    public ApiExceptionResponse(String fieldName, String message, String messageCode) {
        this.fieldName = fieldName;
        this.message = message;
        this.messageCode = messageCode;
    }

    public ApiExceptionResponse(String message, String messageCode){
        this.message = message;
        this.messageCode = messageCode;
    }
}
