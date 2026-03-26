package com.bancolombia.chocolatinazo.infrastructure.exception;

/**
 * Custom exception for unauthorized access attempts.
 * Handled by {@link GlobalExceptionHandler} and returns HTTP 403 (Forbidden).
 * Thrown when a user tries to perform an operation without the required role.
 */
public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message) {
        super(message);
    }
}

