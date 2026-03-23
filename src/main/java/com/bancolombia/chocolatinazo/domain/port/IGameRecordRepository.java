package com.bancolombia.chocolatinazo.domain.port;

import com.bancolombia.chocolatinazo.domain.model.GameRecord;

import java.util.List;
import java.util.UUID;

public interface IGameRecordRepository {
    GameRecord save(GameRecord gameRecord);
    List<GameRecord> findAll();
    List<GameRecord> findByGameId(UUID gameId);
    void deleteAll();
    int count();


}
