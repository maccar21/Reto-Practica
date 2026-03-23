package com.bancolombia.chocolatinazo.domain.port;

import com.bancolombia.chocolatinazo.domain.model.User;

import java.util.Optional;

public interface IUserRepository {
    User save(User user);
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
}
