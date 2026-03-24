package com.bancolombia.chocolatinazo.infrastructure.repository;

import com.bancolombia.chocolatinazo.domain.model.ChocolatinaConfig;
import com.bancolombia.chocolatinazo.domain.port.IChocolatinaConfigRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * JPA implementation of IChocolatinaConfigRepository.
 * Extends JpaRepository for database operations and implements IChocolatinaConfigRepository contract.
 * This maintains the Clean Architecture separation: infrastructure implements domain ports.
 */
@Repository
public interface ChocolatinaConfigRepository extends JpaRepository<ChocolatinaConfig, UUID>, IChocolatinaConfigRepository {

    @Query(value = "SELECT * FROM chocolatina_config ORDER BY updated_at DESC LIMIT 1", nativeQuery = true)
    Optional<ChocolatinaConfig> findLatest();

}
