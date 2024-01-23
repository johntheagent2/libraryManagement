package org.example.librarymanagement.exception;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorAPIResponse {
    private final String errorCode;
    private final String message;
}
