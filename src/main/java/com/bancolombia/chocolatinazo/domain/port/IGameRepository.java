package com.bancolombia.chocolatinazo.domain.port;

import com.bancolombia.chocolatinazo.domain.enums.GameStatus;
import com.bancolombia.chocolatinazo.domain.model.Game;

import java.util.Optional;

/**
 * Port interface for Game persistence operations.
 * Part of the domain layer — decouples business logic from the JPA infrastructure.
 * Implemented by {@link com.bancolombia.chocolatinazo.infrastructure.repository.GameRepository}.
 */
public interface IGameRepository {

    /** Persist a new or updated game. */
    Game save(Game game);

    /** Find a game by its status (e.g., ACTIVE). Only one game should be ACTIVE at a time. */
    Optional<Game> findByStatus(GameStatus status);

    /** Find the most recent game with the given status, ordered by creation date descending. */
    Optional<Game> findFirstByStatusOrderByCreatedAtDesc(GameStatus status);
}
