package org.example.librarymanagement.exception;

import jakarta.mail.MessagingException;
import org.example.librarymanagement.exception.dto.ApiExceptionResponse;
import org.example.librarymanagement.exception.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {ApiRequestException.class, MethodArgumentNotValidException.class})
    public ResponseEntity<List<ApiExceptionResponse>> handleInvalidArgument(MethodArgumentNotValidException exception){
        ArrayList<ApiExceptionResponse> apiExceptionResponses = new ArrayList<>();

        exception.getBindingResult()
                .getFieldErrors()
                .forEach(error -> apiExceptionResponses.add(new ApiExceptionResponse(
                        error.getField(),
                        error.getDefaultMessage(),
                        error.getObjectName() + ".error." + error.getCode()
                )));

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(apiExceptionResponses);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = {NotFoundException.class})
    public ResponseEntity<ApiExceptionResponse> handleNotFoundException(NotFoundException exception){
        ApiExceptionResponse apiExceptionResponse = new ApiExceptionResponse(exception.getMessage(), exception.getErrorCode());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(apiExceptionResponse);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {BadRequestException.class})
    public ResponseEntity<ApiExceptionResponse> handleBadRequestException(BadRequestException exception){
        ApiExceptionResponse apiExceptionResponse = new ApiExceptionResponse(exception.getMessage(), exception.getErrorCode());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(apiExceptionResponse);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = {IOException.class})
    public ResponseEntity<ApiExceptionResponse> handleIOException(IOException exception) {
        ApiExceptionResponse apiExceptionResponse = new ApiExceptionResponse(exception.getMessage(), "IO_ERROR_CODE");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(apiExceptionResponse);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = {TemplateException.class})
    public ResponseEntity<ApiExceptionResponse> handleTemplateException(TemplateException exception) {
        ApiExceptionResponse apiExceptionResponse = new ApiExceptionResponse(exception.getMessage(), "TEMPLATE_ERROR_CODE");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(apiExceptionResponse);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = {MessagingException.class})
    public ResponseEntity<ApiExceptionResponse> handleMessagingException(MessagingException exception) {
        ApiExceptionResponse apiExceptionResponse = new ApiExceptionResponse(exception.getMessage(), "MESSAGING_ERROR_CODE");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(apiExceptionResponse);
    }
}
