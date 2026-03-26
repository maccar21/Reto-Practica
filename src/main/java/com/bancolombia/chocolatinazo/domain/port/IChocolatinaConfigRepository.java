package com.bancolombia.chocolatinazo.domain.port;

import com.bancolombia.chocolatinazo.domain.model.ChocolatinaConfig;

import java.util.Optional;

/**
 * Port interface for ChocolatinaConfig persistence operations.
 * Part of the domain layer — decouples business logic from the JPA infrastructure.
 * Implemented by {@link com.bancolombia.chocolatinazo.infrastructure.repository.ChocolatinaConfigRepository}.
 */
public interface IChocolatinaConfigRepository {

    /** Persist a new or updated chocolatina price configuration. */
    ChocolatinaConfig save(ChocolatinaConfig config);

    /** Find the most recent chocolatina price configuration (ordered by updatedAt descending). */
    Optional<ChocolatinaConfig> findLatest();
}
