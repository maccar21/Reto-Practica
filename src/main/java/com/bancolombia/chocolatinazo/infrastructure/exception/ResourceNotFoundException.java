package com.bancolombia.chocolatinazo.infrastructure.exception;

/**
 * Custom exception for resource not found scenarios.
 * Handled by {@link GlobalExceptionHandler} and returns HTTP 404 (Not Found).
 * Examples: user not found, no active game, chocolatina price not configured.
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}

