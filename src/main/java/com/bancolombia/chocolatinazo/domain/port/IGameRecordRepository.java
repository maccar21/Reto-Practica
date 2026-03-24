package com.bancolombia.chocolatinazo.domain.port;

import com.bancolombia.chocolatinazo.domain.model.GameRecord;

import java.util.List;
import java.util.UUID;

/**
 * Port interface for GameRecord repository operations.
 * Defines custom query methods specific to the domain.
 * Basic CRUD operations (save, delete, etc.) are inherited from JpaRepository.
 */
public interface IGameRecordRepository {
    List<GameRecord> findByGameId(UUID gameId);

}
