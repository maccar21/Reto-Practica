package com.bancolombia.chocolatinazo.infrastructure.exception;

/**
 * Exception thrown when user input validation fails (400).
 */
public class InvalidInputException extends RuntimeException {
    public InvalidInputException(String message) {
        super(message);
    }
}

