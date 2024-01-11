package org.example.librarymanagement.exception;

import org.example.librarymanagement.exception.dto.ApiExceptionResponse;
import org.example.librarymanagement.exception.exception.ApiRequestException;
import org.example.librarymanagement.exception.exception.BadRequestException;
import org.example.librarymanagement.exception.exception.NotFoundException;
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
}
