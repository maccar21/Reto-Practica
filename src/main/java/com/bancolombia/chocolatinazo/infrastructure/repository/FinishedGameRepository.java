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

    // Query by loser (User relationship) - use underscore notation to access related entity's ID
    List<FinishedGame> findByLoser_Id(UUID loserId);

    // Query by game (Game relationship) - use underscore notation to access related entity's ID
    List<FinishedGame> findByGame_Id(UUID gameId);

    // Query by loser with ordering by finishedAt descending
    List<FinishedGame> findByLoser_IdOrderByFinishedAtDesc(UUID loserId);

    // Query by game with ordering by finishedAt descending
    List<FinishedGame> findByGame_IdOrderByFinishedAtDesc(UUID gameId);

}
