package com.example.demo.exceptions;

public class BadRequestException extends Exception{
    public BadRequestException(String message){
        super(message);
    }
}
