package com.bancolombia.chocolatinazo.infrastructure.exception;

/**
 * Exception thrown when a resource is not found (404).
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}

