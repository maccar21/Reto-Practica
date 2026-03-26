package com.bancolombia.chocolatinazo.application.service;

import com.bancolombia.chocolatinazo.application.dto.response.UserResponse;
import com.bancolombia.chocolatinazo.domain.enums.RoleName;
import com.bancolombia.chocolatinazo.domain.model.Role;
import com.bancolombia.chocolatinazo.domain.model.User;
import com.bancolombia.chocolatinazo.domain.port.IRoleRepository;
import com.bancolombia.chocolatinazo.domain.port.IUserRepository;
import com.bancolombia.chocolatinazo.infrastructure.exception.InvalidInputException;
import com.bancolombia.chocolatinazo.infrastructure.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Service responsible for managing user roles.
 * Handles adding roles to existing users with validation and error handling.
 * Uses domain port interfaces to maintain Clean Architecture separation.
 */
@Service
public class AddRoleService {

    private final IUserRepository userRepository;
    private final IRoleRepository roleRepository;

    public AddRoleService(IUserRepository userRepository, IRoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    /**
     * Add a role to an existing user.
     * Validates that the user exists, the role name is valid, the role exists in DB,
     * and that the user doesn't already have the role.
     *
     * @param userId The UUID of the user to update
     * @param roleName The name of the role to add (PLAYER, AUDITOR, ADMIN)
     * @return UserResponse with updated roles
     * @throws ResourceNotFoundException if user not found
     * @throws InvalidInputException if role name is invalid, role not found, or user already has the role
     */
    public UserResponse addRoleToUser(UUID userId, String roleName) {
        // Step 1: Find user by ID — throws 404 if user does not exist
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        // Step 2: Convert the role name string to the RoleName enum — throws 400 if invalid
        RoleName roleNameEnum;
        try {
            roleNameEnum = RoleName.valueOf(roleName.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidInputException("Invalid role name: " + roleName + ". Valid roles are: PLAYER, AUDITOR, ADMIN");
        }

        // Step 3: Find the role entity in the database — throws 400 if role not seeded
        Role role = roleRepository.findByName(roleNameEnum)
                .orElseThrow(() -> new InvalidInputException("Role not found: " + roleName));

        // Step 4: Check if the user already has this role to prevent duplicates
        boolean alreadyHasRole = user.getRoles().stream()
                .anyMatch(r -> r.getName() == roleNameEnum);
        if (alreadyHasRole) {
            throw new InvalidInputException("User already has role: " + roleName);
        }

        // Step 5: Add the new role to the user's role set and persist the change
        user.getRoles().add(role);
        User updatedUser = userRepository.save(user);

        // Step 6: Return the updated user response with all current roles
        return UserResponse.fromUser(updatedUser);
    }
}

