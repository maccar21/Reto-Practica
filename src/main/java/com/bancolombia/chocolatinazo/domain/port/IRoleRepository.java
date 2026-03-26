package com.bancolombia.chocolatinazo.domain.port;

import com.bancolombia.chocolatinazo.domain.enums.RoleName;
import com.bancolombia.chocolatinazo.domain.model.Role;

import java.util.Optional;

/**
 * Port interface for Role persistence operations.
 * Part of the domain layer — decouples business logic from the JPA infrastructure.
 * Implemented by {@link com.bancolombia.chocolatinazo.infrastructure.repository.RoleRepository}.
 */
public interface IRoleRepository {

    /**
     * Find a role by its name.
     *
     * @param name The role name enum (PLAYER, AUDITOR, ADMIN)
     * @return Optional containing the role if found
     */
    Optional<Role> findByName(RoleName name);
}
