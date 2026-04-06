package com.microservice.exception;

public class DuplicateAccountException extends RuntimeException{

    public DuplicateAccountException(String message){
        super(message);
    }
}