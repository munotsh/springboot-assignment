package com.example.assignment.exception;

public class DataException extends RuntimeException{
    public DataException(String msg){
        super(msg);
    }
    public DataException(String msg, Throwable th){
        super(msg,th);
    }
}
