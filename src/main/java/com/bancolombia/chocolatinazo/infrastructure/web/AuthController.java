package com.bancolombia.chocolatinazo.infrastructure.web;

import com.bancolombia.chocolatinazo.application.dto.request.AuthLoginRequest;
import com.bancolombia.chocolatinazo.application.dto.request.AuthRegisterRequest;
import com.bancolombia.chocolatinazo.application.dto.response.AuthLoginResponse;
import com.bancolombia.chocolatinazo.application.dto.response.UserResponse;
import com.bancolombia.chocolatinazo.application.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST Controller for authentication endpoints.
 * Provides user registration and login functionality.
 * These endpoints are publicly accessible (no JWT token required).
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService)   {
        this.authService = authService;
    }

    /**
     * Register a new user.
     * Public endpoint - no authentication required.
     *
     * @param request The registration request (username, email, password)
     * @return UserResponse with the created user's information
     * @throws IllegalArgumentException if username or email already exists
     */
    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody AuthRegisterRequest request) {
        UserResponse response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Login user and return JWT token.
     * Public endpoint - no authentication required.
     *
     * @param request The login request (email or username + password)
     * @return AuthLoginResponse with JWT token and user information
     * @throws IllegalArgumentException if credentials are invalid
     */
    @PostMapping("/login")
    public ResponseEntity<AuthLoginResponse> login(@Valid @RequestBody AuthLoginRequest request) {
        AuthLoginResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }
}

