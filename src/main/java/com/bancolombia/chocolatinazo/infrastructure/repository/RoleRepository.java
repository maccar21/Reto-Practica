package com.bancolombia.chocolatinazo.infrastructure.repository;

import com.bancolombia.chocolatinazo.domain.enums.RoleName;
import com.bancolombia.chocolatinazo.domain.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {

    Optional<Role> findByName(RoleName name);

}
