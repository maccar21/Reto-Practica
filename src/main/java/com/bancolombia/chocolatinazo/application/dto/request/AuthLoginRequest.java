package com.bancolombia.chocolatinazo.application.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * Request DTO for user login.
 * Supports flexible authentication: the user can log in using either email or username.
 * If the "email" field contains a value without '@', it is treated as a username.
 */
public class AuthLoginRequest {

    @Email(message = "Email format is invalid")
    @Pattern(
        regexp = "^$|^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$",
        message = "Email must have a valid domain (e.g. user@example.com)"
    )
    private String email;
    private String username;

    @NotBlank(message = "Password is required")
    private String password;

    public AuthLoginRequest() {
    }

    public AuthLoginRequest(String email, String username, String password) {
        this.email = email;
        this.username = username;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

