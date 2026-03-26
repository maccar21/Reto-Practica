package com.bancolombia.chocolatinazo.application.dto.request;

/**
 * Request DTO for picking a chocolatina.
 * No body fields are required — the authenticated user is extracted from the JWT token
 * and the active game is resolved automatically.
 * This DTO exists for architectural symmetry and future extensibility.
 */
public class PickChocolatinaRequest {
}

