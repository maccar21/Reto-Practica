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

    /** {@inheritDoc} */
    @Override
    List<GameRecord> findByGame_Id(UUID gameId);

    /** Find all game records for a specific user across all games. */
    List<GameRecord> findByUser_Id(UUID userId);

    /** Find all game records for a specific game, ordered by pick time descending. */
    List<GameRecord> findByGame_IdOrderByPickedAtDesc(UUID gameId);

    /** {@inheritDoc} */
    @Override
    boolean existsByGame_IdAndUser_Id(UUID gameId, UUID userId);

    @Override
    boolean existsByGame_IdAndChocolatinaNumber(UUID gameId, int chocolatinaNumber);

}
