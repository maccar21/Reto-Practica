package com.bancolombia.chocolatinazo.domain.port;

import com.bancolombia.chocolatinazo.domain.model.User;

import java.util.Optional;
import java.util.UUID;

/**
 * Port interface for User repository operations.
 * Defines all repository operations needed by the application layer.
 * Implementation is in infrastructure/repository/UserRepository.
 */
public interface IUserRepository {
    // CRUD operations
    User save(User user);
    Optional<User> findById(UUID id);

    // Custom query methods
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
}
