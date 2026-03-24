package com.bancolombia.chocolatinazo.domain.port;

import com.bancolombia.chocolatinazo.domain.model.ChocolatinaConfig;

import java.util.Optional;

/**
 * Port interface for ChocolatinaConfig repository operations.
 * Defines custom query methods specific to the domain.
 * Basic CRUD operations (save, delete, etc.) are inherited from JpaRepository.
 */
public interface IChocolatinaConfigRepository {
    Optional<ChocolatinaConfig> findLatest();
}
