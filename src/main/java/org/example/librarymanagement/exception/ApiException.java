package org.example.librarymanagement.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiException {
    private String fieldName;
    private String message;
    private String messageCode;

    public ApiException(String fieldName, String message, String messageCode) {
        this.fieldName = fieldName;
        this.message = message;
        this.messageCode = messageCode;
    }

    public ApiException(String message, String messageCode){
        this.message = message;
        this.messageCode = messageCode;
    }
}
