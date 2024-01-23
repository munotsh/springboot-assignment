package com.example.assignment.exception;

public class ApiInternalServerException extends RuntimeException{

    public ApiInternalServerException(String message){
        super(message);
    }
    public  ApiInternalServerException(String message,Throwable throwable){
        super(message,throwable);
    }
}
