package com.bancolombia.chocolatinazo.infrastructure.exception;

/**
 * Custom exception for invalid user input scenarios.
 * Handled by {@link GlobalExceptionHandler} and returns HTTP 400 (Bad Request).
 * Examples: duplicate username, invalid role name, already picked chocolatina.
 */
public class InvalidInputException extends RuntimeException {
    public InvalidInputException(String message) {
        super(message);
    }
}

