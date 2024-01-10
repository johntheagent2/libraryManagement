package org.example.librarymanagement.exception;

import org.example.librarymanagement.exception.serviceException.ServiceException;
import org.example.librarymanagement.exception.validationException.ApiRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {ApiRequestException.class, MethodArgumentNotValidException.class})
    public Map<Map<String, Object>, ArrayList<ApiException>> handleInvalidArgument(MethodArgumentNotValidException exception){
        Map<Map<String, Object>, ArrayList<ApiException>> errorMap = new HashMap<>();

        Map<String, Object> apiHeader;
        ArrayList<ApiException> apiExceptions;

        apiHeader = createApiHeader(HttpStatus.BAD_REQUEST);
        apiExceptions = createApiExceptionList(exception);

        errorMap.put(apiHeader, apiExceptions);

        return errorMap;
    }

    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    @ExceptionHandler(Exception.class)
    public Map<Map<String, Object>, ArrayList<ApiException>> handleTokenException(ServiceException exception){
        Map<Map<String, Object>, ArrayList<ApiException>> errorMap = new HashMap<>();

        Map<String, Object> apiHeader = createApiHeader(HttpStatus.NOT_ACCEPTABLE);
        ArrayList<ApiException> apiExceptions = new ArrayList<>();

        apiExceptions.add(new ApiException(
                exception.getMessage(),
                exception.getMessageKey()));

        errorMap.put(apiHeader, apiExceptions);

        return errorMap;
    }

    public ArrayList<ApiException> createApiExceptionList(MethodArgumentNotValidException e){
        ArrayList<ApiException> apiExceptions = new ArrayList<>();

        e.getBindingResult()
                .getFieldErrors()
                .forEach(error -> apiExceptions.add(new ApiException(
                        error.getField(),
                        error.getDefaultMessage(),
                        error.getObjectName() + ".error." + error.getCode()
                )));

        return apiExceptions;
    }

    public Map<String, Object> createApiHeader(HttpStatus status){
        Map<String, Object> apiHeader = new HashMap<>();

        apiHeader.put("HTTP status", status);
        apiHeader.put("Timestamp",ZonedDateTime.now());
        return apiHeader;
    }
}
