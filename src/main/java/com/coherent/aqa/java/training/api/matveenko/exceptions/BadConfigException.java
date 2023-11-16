package com.coherent.aqa.java.training.api.matveenko.exceptions;

public class BadConfigException extends RuntimeException{

    public BadConfigException(String message){
        super(message);
    }

    public BadConfigException(String message, Throwable cause){
        super(message, cause);
    }
}