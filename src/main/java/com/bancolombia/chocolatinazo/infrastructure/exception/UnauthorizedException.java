package com.bancolombia.chocolatinazo.infrastructure.exception;

/**
 * Exception thrown when user attempts operation without proper authorization (403).
 */
public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message) {
        super(message);
    }
}

