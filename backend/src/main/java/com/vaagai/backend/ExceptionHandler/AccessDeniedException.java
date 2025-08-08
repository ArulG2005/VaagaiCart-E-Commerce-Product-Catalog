package com.vaagai.backend.ExceptionHandler;
public class AccessDeniedException extends RuntimeException{
    
    public AccessDeniedException(String message){
        super(message);
    }
    
}