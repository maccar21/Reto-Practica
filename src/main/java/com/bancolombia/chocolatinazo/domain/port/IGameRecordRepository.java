package com.bancolombia.chocolatinazo.domain.port;

import com.bancolombia.chocolatinazo.domain.model.GameRecord;

import java.util.List;
import java.util.UUID;

/**
 * Port interface for GameRecord repository operations.
 */
public interface IGameRecordRepository {
    GameRecord save(GameRecord gameRecord);
    List<GameRecord> findByGame_Id(UUID gameId);
    void deleteAll();
}
