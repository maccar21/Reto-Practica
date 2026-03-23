package com.bancolombia.chocolatinazo.infrastructure.repository;

import com.bancolombia.chocolatinazo.domain.model.GameRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface GameRecordRepository extends JpaRepository<GameRecord, UUID> {

    List<GameRecord> findByGameId(UUID gameId);

    List<GameRecord> findByUserId(UUID userId);

    List<GameRecord> findByGameIdOrderByPickedAtDesc(UUID gameId);

}
