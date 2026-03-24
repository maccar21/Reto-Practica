package com.bancolombia.chocolatinazo.application.service;

import com.bancolombia.chocolatinazo.application.dto.request.AuthLoginRequest;
import com.bancolombia.chocolatinazo.application.dto.request.AuthRegisterRequest;
import com.bancolombia.chocolatinazo.application.dto.response.AuthLoginResponse;
import com.bancolombia.chocolatinazo.application.dto.response.UserResponse;
import com.bancolombia.chocolatinazo.domain.enums.RoleName;
import com.bancolombia.chocolatinazo.domain.model.Role;
import com.bancolombia.chocolatinazo.domain.model.User;
import com.bancolombia.chocolatinazo.infrastructure.repository.RoleRepository;
import com.bancolombia.chocolatinazo.infrastructure.repository.UserRepository;
import com.bancolombia.chocolatinazo.infrastructure.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service responsible for user authentication (register and login).
 * Handles user registration with password encryption and role assignment,
 * and user login with JWT token generation.
 */
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, RoleRepository roleRepository,
                       PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    /**
     * Register a new user in the system.
     * Validates that username and email are unique, encrypts the password,
     * assigns the default PLAYER role, and saves the user.
     *
     * @param request The registration request containing username, email, and password
     * @return UserResponse with the created user's information
     * @throws IllegalArgumentException if username or email already exists
     */
    public UserResponse register(AuthRegisterRequest request) {
        // Validate username not already exists
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }

        // Validate email not already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        // Get default role (PLAYER)
        Role playerRole = roleRepository.findByName(RoleName.PLAYER)
                .orElseThrow(() -> new IllegalStateException("PLAYER role not found in database"));

        // Create new user
        User newUser = new User();
        newUser.setUsername(request.getUsername());
        newUser.setEmail(request.getEmail());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        newUser.setRole(playerRole);

        // Save user to database
        User savedUser = userRepository.save(newUser);

        // Return UserResponse
        return UserResponse.fromUser(savedUser);
    }

    /**
     * Authenticate a user and generate a JWT token.
     * Searches for the user by email or username, validates the password,
     * and generates a JWT token containing the user's ID and role.
     *
     * @param request The login request containing email/username and password
     * @return AuthLoginResponse with JWT token and user information
     * @throws IllegalArgumentException if credentials are invalid
     */
    public AuthLoginResponse login(AuthLoginRequest request) {
        // Search for user by email or username
        User user = null;

        if (request.getEmail() != null && !request.getEmail().isBlank()) {
            Optional<User> userByEmail = userRepository.findByEmail(request.getEmail());
            if (userByEmail.isPresent()) {
                user = userByEmail.get();
            }
        }

        if (user == null && request.getUsername() != null && !request.getUsername().isBlank()) {
            Optional<User> userByUsername = userRepository.findByUsername(request.getUsername());
            if (userByUsername.isPresent()) {
                user = userByUsername.get();
            }
        }

        // Validate user exists
        if (user == null) {
            throw new IllegalArgumentException("Invalid email/username or password");
        }

        // Validate password
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid email/username or password");
        }

        // Generate JWT token
        String role = user.getRole().getName().toString();
        String token = jwtService.generateToken(user.getId(), role, user.getUsername());

        // Create and return response
        UserResponse userResponse = UserResponse.fromUser(user);
        return new AuthLoginResponse(token, userResponse);
    }
}

