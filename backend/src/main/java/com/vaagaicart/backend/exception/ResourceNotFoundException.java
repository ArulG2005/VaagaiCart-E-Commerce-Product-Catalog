package com.vaagaicart.backend.exception;



public class ResourceNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L; // Optional for serialization warnings

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
