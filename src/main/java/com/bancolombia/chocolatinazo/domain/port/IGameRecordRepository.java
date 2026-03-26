package com.bancolombia.chocolatinazo.domain.port;

import com.bancolombia.chocolatinazo.domain.model.GameRecord;

import java.util.List;
import java.util.UUID;

/**
 * Port interface for GameRecord persistence operations.
 * Part of the domain layer — decouples business logic from the JPA infrastructure.
 * Implemented by {@link com.bancolombia.chocolatinazo.infrastructure.repository.GameRecordRepository}.
 */
public interface IGameRecordRepository {

    /** Persist a new game record (player's chocolatina pick). */
    GameRecord save(GameRecord gameRecord);

    /** Find all game records for a specific game. */
    List<GameRecord> findByGame_Id(UUID gameId);

    /** Check if a player has already picked a chocolatina in a specific game. */
    boolean existsByGame_IdAndUser_Id(UUID gameId, UUID userId);

    /** Check if a chocolatina number has already been picked in a specific game. */
    boolean existsByGame_IdAndChocolatinaNumber(UUID gameId, int chocolatinaNumber);

    /** Delete all game records — used to clean up after a game is finished. */
    void deleteAll();
}
