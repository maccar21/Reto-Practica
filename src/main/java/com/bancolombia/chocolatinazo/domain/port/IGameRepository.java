package com.bancolombia.chocolatinazo.domain.port;

import com.bancolombia.chocolatinazo.domain.enums.GameStatus;
import com.bancolombia.chocolatinazo.domain.model.Game;

import java.util.Optional;

/**
 * Port interface for Game repository operations.
 * Defines custom query methods specific to the domain.
 * Basic CRUD operations are inherited from JpaRepository.
 */
public interface IGameRepository {
    Optional<Game> findByStatus(GameStatus status);
    Optional<Game> findFirstByStatusOrderByCreatedAtDesc(GameStatus status);
}
