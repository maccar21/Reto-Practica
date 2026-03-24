package com.bancolombia.chocolatinazo.infrastructure.repository;

import com.bancolombia.chocolatinazo.domain.model.GameRecord;
import com.bancolombia.chocolatinazo.domain.port.IGameRecordRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * JPA implementation of IGameRecordRepository.
 * Extends JpaRepository for database operations and implements IGameRecordRepository contract.
 * This maintains the Clean Architecture separation: infrastructure implements domain ports.
 */
@Repository
public interface GameRecordRepository extends JpaRepository<GameRecord, UUID>, IGameRecordRepository {

    List<GameRecord> findByGameId(UUID gameId);

    List<GameRecord> findByUserId(UUID userId);

    List<GameRecord> findByGameIdOrderByPickedAtDesc(UUID gameId);

}
