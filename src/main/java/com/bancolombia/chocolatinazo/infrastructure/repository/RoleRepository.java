package com.bancolombia.chocolatinazo.infrastructure.repository;

import com.bancolombia.chocolatinazo.domain.enums.RoleName;
import com.bancolombia.chocolatinazo.domain.model.Role;
import com.bancolombia.chocolatinazo.domain.port.IRoleRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * JPA implementation of IRoleRepository.
 * Extends JpaRepository for database operations and implements IRoleRepository contract.
 * This maintains the Clean Architecture separation: infrastructure implements domain ports.
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, UUID>, IRoleRepository {

    Optional<Role> findByName(RoleName name);

}
