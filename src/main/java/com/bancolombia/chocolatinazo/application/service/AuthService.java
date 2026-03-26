package com.bancolombia.chocolatinazo.application.service;

import com.bancolombia.chocolatinazo.application.dto.request.AuthLoginRequest;
import com.bancolombia.chocolatinazo.application.dto.request.AuthRegisterRequest;
import com.bancolombia.chocolatinazo.application.dto.response.AuthLoginResponse;
import com.bancolombia.chocolatinazo.application.dto.response.UserResponse;
import com.bancolombia.chocolatinazo.domain.enums.RoleName;
import com.bancolombia.chocolatinazo.domain.model.Role;
import com.bancolombia.chocolatinazo.domain.model.User;
import com.bancolombia.chocolatinazo.domain.port.IRoleRepository;
import com.bancolombia.chocolatinazo.domain.port.IUserRepository;
import com.bancolombia.chocolatinazo.infrastructure.exception.InvalidInputException;
import com.bancolombia.chocolatinazo.infrastructure.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Service responsible for user authentication (register and login).
 * Handles user registration with password encryption and role assignment,
 * and user login with JWT token generation.
 */
@Service
public class AuthService {

    private final IUserRepository userRepository;
    private final IRoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(IUserRepository userRepository, IRoleRepository roleRepository,
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
     * @throws InvalidInputException if username or email already exists
     */
    public UserResponse register(AuthRegisterRequest request) {
        // Validate username not already exists
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new InvalidInputException("Username already exists");
        }

        // Validate email not already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new InvalidInputException("Email already exists");
        }

        // Get default role (PLAYER)
        Role playerRole = roleRepository.findByName(RoleName.PLAYER)
                .orElseThrow(() -> new InvalidInputException("PLAYER role not found in database"));

        // Create new user
        User newUser = new User();
        newUser.setUsername(request.getUsername());
        newUser.setEmail(request.getEmail());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        newUser.setRoles(new HashSet<>(Set.of(playerRole)));

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
     * @throws InvalidInputException if credentials are invalid
     */
    public AuthLoginResponse login(AuthLoginRequest request) {
        // Step 1: Find user by email or username
        User user = findUserByCredentials(request)
                .orElseThrow(() -> new InvalidInputException("Invalid email/username or password"));

        // Step 2: Validate that the provided password matches the stored encrypted password
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidInputException("Invalid email/username or password");
        }

        // Step 3: Generate JWT token containing userId, roles, and username as claims
        List<String> roles = user.getRoles().stream()
                .map(role -> role.getName().toString())
                .toList();
        String token = jwtService.generateToken(user.getId(), roles, user.getUsername());

        // Step 4: Build and return the authentication response with token and user info
        UserResponse userResponse = UserResponse.fromUser(user);
        return new AuthLoginResponse(token, userResponse);
    }

    /**
     * Find a user by email or username from the login request.
     * If the email field contains '@', it searches by email.
     * If it doesn't contain '@', it treats it as a username (flexible login).
     * Falls back to the username field if email lookup fails.
     *
     * @param request The login request with email/username fields
     * @return Optional containing the found user, or empty if not found
     */
    private Optional<User> findUserByCredentials(AuthLoginRequest request) {
        // Try to find user by email field (can be email or username)
        if (request.getEmail() != null && !request.getEmail().isBlank()) {
            Optional<User> found = findByEmailField(request.getEmail());
            if (found.isPresent()) {
                return found;
            }
        }

        // Fallback: try to find by the username field
        if (request.getUsername() != null && !request.getUsername().isBlank()) {
            return userRepository.findByUsername(request.getUsername());
        }

        return Optional.empty();
    }

    /**
     * Resolve a user from the email field value.
     * If it contains '@', validates format and searches by email.
     * Otherwise, treats it as a username.
     *
     * @param emailFieldValue The value from the email field in the login request
     * @return Optional containing the found user
     */
    private Optional<User> findByEmailField(String emailFieldValue) {
        if (emailFieldValue.contains("@")) {
            if (!emailFieldValue.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
                throw new InvalidInputException("Invalid email/username or password");
            }
            return userRepository.findByEmail(emailFieldValue);
        }
        // No '@' found — treat as username (flexible login)
        return userRepository.findByUsername(emailFieldValue);
    }
}
