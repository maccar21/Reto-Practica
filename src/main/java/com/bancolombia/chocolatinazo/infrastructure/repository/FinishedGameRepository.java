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

    List<FinishedGame> findByLoserId(UUID loserId);

    List<FinishedGame> findByGameId(UUID gameId);

    List<FinishedGame> findByLoserIdOrderByFinishedAtDesc(UUID loserId);

    List<FinishedGame> findByGameIdOrderByFinishedAtDesc(UUID gameId);

}
