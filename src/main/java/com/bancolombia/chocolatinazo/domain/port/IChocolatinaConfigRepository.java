package com.bancolombia.chocolatinazo.domain.port;

import com.bancolombia.chocolatinazo.domain.model.ChocolatinaConfig;

import java.util.Optional;

public interface IChocolatinaConfigRepository {
    ChocolatinaConfig save(ChocolatinaConfig chocolatinaConfig);
    Optional<ChocolatinaConfig> findLatest();
}
