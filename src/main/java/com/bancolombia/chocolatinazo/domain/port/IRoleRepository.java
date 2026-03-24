package com.bancolombia.chocolatinazo.domain.port;

import com.bancolombia.chocolatinazo.domain.enums.RoleName;
import com.bancolombia.chocolatinazo.domain.model.Role;

import java.util.Optional;

/**
 * Port interface for Role repository operations.
 * Defines custom query methods specific to the domain.
 * Basic CRUD operations are inherited from JpaRepository.
 */
public interface IRoleRepository {
    Optional<Role> findByName(RoleName name);
}
