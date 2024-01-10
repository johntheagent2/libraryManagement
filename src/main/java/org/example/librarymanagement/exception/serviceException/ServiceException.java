package org.example.librarymanagement.exception.serviceException;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ServiceException extends RuntimeException {

    private final String messageKey;

    public ServiceException(String messageKey, String message) {
        super(message);
        this.messageKey = messageKey;
    }
}
