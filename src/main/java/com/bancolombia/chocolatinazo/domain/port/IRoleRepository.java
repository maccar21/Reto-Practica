package com.bancolombia.chocolatinazo.domain.port;

import com.bancolombia.chocolatinazo.domain.enums.RoleName;
import com.bancolombia.chocolatinazo.domain.model.Role;

import java.util.Optional;
import java.util.UUID;

/**
 * Port interface for Role repository operations.
 * Defines all repository operations needed by the application layer.
 * Implementation is in infrastructure/repository/RoleRepository.
 */
public interface IRoleRepository {
    // CRUD operations
    Optional<Role> findById(UUID id);

    // Custom query methods
    Optional<Role> findByName(RoleName name);
}
