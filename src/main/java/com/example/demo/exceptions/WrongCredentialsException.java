package com.example.demo.exceptions;

public class WrongCredentialsException extends Exception{
    public WrongCredentialsException(String message) {
        super(message);
    }
}
