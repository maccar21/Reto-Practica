package com.bancolombia.chocolatinazo.domain.port;

import com.bancolombia.chocolatinazo.domain.model.GameRecord;

import java.util.List;

public interface IGameRecordRepository {
    GameRecord save(GameRecord gameRecord);
    List<GameRecord> findAll();
    void deleteAll();
    int count();


}
