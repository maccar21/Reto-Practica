package com.bancolombia.chocolatinazo.application.dto.response;

import com.bancolombia.chocolatinazo.domain.model.User;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Response DTO representing a user's public profile.
 * Contains the user's ID, username, email, assigned roles, and registration timestamp.
 * Includes a factory method to convert a User entity to this DTO, ensuring
 * that sensitive data (like passwords) is never exposed in API responses.
 */
public class UserResponse {

    private UUID id;
    private String username;
    private String email;
    private Set<String> roles;
    private LocalDateTime createdAt;

    public UserResponse() {
    }

    public UserResponse(UUID id, String username, String email, Set<String> roles, LocalDateTime createdAt) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.roles = roles;
        this.createdAt = createdAt;
    }

    /**
     * Factory method that converts a User domain entity into a UserResponse DTO.
     * Maps the user's role set to a set of role name strings.
     *
     * @param user The User entity to convert
     * @return UserResponse DTO with the user's public information
     */
    public static UserResponse fromUser(User user) {
        Set<String> roleNames = user.getRoles().stream()
                .map(r -> r.getName().name())
                .collect(Collectors.toSet());

        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                roleNames,
                user.getCreatedAt()
        );
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}

