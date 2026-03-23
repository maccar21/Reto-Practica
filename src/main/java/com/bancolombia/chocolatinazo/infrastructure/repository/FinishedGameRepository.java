package com.bancolombia.chocolatinazo.infrastructure.repository;

import com.bancolombia.chocolatinazo.domain.model.FinishedGame;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FinishedGameRepository extends JpaRepository<FinishedGame, UUID> {

    List<FinishedGame> findByLoserId(UUID loserId);

    List<FinishedGame> findByGameId(UUID gameId);

    List<FinishedGame> findByLoserIdOrderByFinishedAtDesc(UUID loserId);

    List<FinishedGame> findByGameIdOrderByFinishedAtDesc(UUID gameId);

}
