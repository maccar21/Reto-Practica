package com.bancolombia.chocolatinazo.domain.port;

import com.bancolombia.chocolatinazo.domain.model.ChocolatinaConfig;

import java.util.Optional;

/**
 * Port interface for ChocolatinaConfig repository operations.
 */
public interface IChocolatinaConfigRepository {
    ChocolatinaConfig save(ChocolatinaConfig config);
    Optional<ChocolatinaConfig> findLatest();
}
