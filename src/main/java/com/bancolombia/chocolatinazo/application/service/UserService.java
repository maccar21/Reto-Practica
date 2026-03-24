package com.bancolombia.chocolatinazo.application.service;

import com.bancolombia.chocolatinazo.application.dto.response.UserResponse;
import com.bancolombia.chocolatinazo.domain.enums.RoleName;
import com.bancolombia.chocolatinazo.domain.model.Role;
import com.bancolombia.chocolatinazo.domain.model.User;
import com.bancolombia.chocolatinazo.domain.port.IRoleRepository;
import com.bancolombia.chocolatinazo.domain.port.IUserRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Service responsible for user management operations.
 * Handles adding roles to users.
 */
@Service
public class UserService {

    private final IUserRepository userRepository;
    private final IRoleRepository roleRepository;

    public UserService(IUserRepository userRepository, IRoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    /**
     * Add a role to an existing user.
     *
     * @param userId The UUID of the user
     * @param roleName The name of the role to add
     * @return UserResponse with updated roles
     * @throws RuntimeException if user not found, role name invalid, or role not found
     * @throws IllegalArgumentException if user already has this role
     */
    public UserResponse addRoleToUser(UUID userId, String roleName) {
        // 1. Buscar usuario por ID
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 2. Convertir roleName a enum RoleName
        RoleName roleNameEnum;
        try {
            roleNameEnum = RoleName.valueOf(roleName.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid role name");
        }

        // 3. Buscar rol en la BD
        Role role = roleRepository.findByName(roleNameEnum)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        // 4. Verificar que el usuario no tenga ya ese rol
        boolean alreadyHasRole = user.getRoles().stream()
                .anyMatch(r -> r.getName() == roleNameEnum);
        if (alreadyHasRole) {
            throw new IllegalArgumentException("User already has this role");
        }

        // 5. Agregar rol y guardar
        user.getRoles().add(role);
        User updatedUser = userRepository.save(user);

        // 6. Retornar UserResponse
        return UserResponse.fromUser(updatedUser);
    }
}

