package com.bancolombia.chocolatinazo.domain.port;

import com.bancolombia.chocolatinazo.domain.model.User;

import java.util.Optional;
import java.util.UUID;

/**
 * Port interface for User persistence operations.
 * Part of the domain layer — decouples business logic from the JPA infrastructure.
 * Implemented by {@link com.bancolombia.chocolatinazo.infrastructure.repository.UserRepository}.
 */
public interface IUserRepository {

    /** Persist a new or updated user. */
    User save(User user);

    /** Find a user by their unique ID. */
    Optional<User> findById(UUID id);

    /** Find a user by their email address. */
    Optional<User> findByEmail(String email);

    /** Find a user by their username. */
    Optional<User> findByUsername(String username);

    /** Check if a user with the given email already exists. */
    boolean existsByEmail(String email);

    /** Check if a user with the given username already exists. */
    boolean existsByUsername(String username);
}
