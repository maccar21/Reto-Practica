package com.bancolombia.chocolatinazo.infrastructure.repository;

import com.bancolombia.chocolatinazo.domain.model.FinishedGame;
import com.bancolombia.chocolatinazo.domain.port.IFinishedGameRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * JPA implementation of IFinishedGameRepository.
 * Extends JpaRepository for database operations and implements IFinishedGameRepository contract.
 * This maintains the Clean Architecture separation: infrastructure implements domain ports.
 */
@Repository
public interface FinishedGameRepository extends JpaRepository<FinishedGame, UUID>, IFinishedGameRepository {

    /** Find all finished games where a specific user was the loser. */
    List<FinishedGame> findByLoser_Id(UUID loserId);

    /** Find all finished games for a specific game session. */
    List<FinishedGame> findByGame_Id(UUID gameId);

    /** Find all finished games where a specific user lost, ordered by date descending. */
    List<FinishedGame> findByLoser_IdOrderByFinishedAtDesc(UUID loserId);

    /** Find all finished games for a specific game session, ordered by date descending. */
    List<FinishedGame> findByGame_IdOrderByFinishedAtDesc(UUID gameId);



}
