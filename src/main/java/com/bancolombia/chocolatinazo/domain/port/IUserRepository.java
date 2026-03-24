package com.bancolombia.chocolatinazo.domain.port;

import com.bancolombia.chocolatinazo.domain.model.User;

import java.util.Optional;

/**
 * Port interface for User repository operations.
 * Defines custom query methods specific to the domain.
 * Basic CRUD operations (save, delete, etc.) are inherited from JpaRepository.
 */
public interface IUserRepository {
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
}
