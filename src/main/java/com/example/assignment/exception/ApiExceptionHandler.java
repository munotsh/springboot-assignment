package com.example.assignment.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpServerErrorException;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(value = {ApiRequestException.class})
    public ResponseEntity<Object> handleApiRequestException(ApiRequestException e){
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        ApiException apiException = new ApiException(
                e.getMessage(),
                httpStatus,
                ZonedDateTime.now(ZoneId.of("Z")));
        return new ResponseEntity<>(apiException,httpStatus);
    }

    @ExceptionHandler(value = {ApiInternalServerException.class})
    public ResponseEntity<Object> handleInternalServerError(ApiInternalServerException e){
     HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
     ApiException apiException = new ApiException(e.getLocalizedMessage(),
             httpStatus,
             ZonedDateTime.now(ZoneId.of("Z")));
     return new ResponseEntity<>(apiException,httpStatus);
    }
}
