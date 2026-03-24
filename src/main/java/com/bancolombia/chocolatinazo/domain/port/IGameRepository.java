package com.bancolombia.chocolatinazo.domain.port;

import com.bancolombia.chocolatinazo.domain.enums.GameStatus;
import com.bancolombia.chocolatinazo.domain.model.Game;

import java.util.Optional;

/**
 * Port interface for Game repository operations.
 */
public interface IGameRepository {
    Game save(Game game);
    Optional<Game> findByStatus(GameStatus status);
    Optional<Game> findFirstByStatusOrderByCreatedAtDesc(GameStatus status);
}
