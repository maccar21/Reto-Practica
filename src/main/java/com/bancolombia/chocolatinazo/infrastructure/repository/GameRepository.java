package com.bancolombia.chocolatinazo.infrastructure.repository;

import com.bancolombia.chocolatinazo.domain.enums.GameStatus;
import com.bancolombia.chocolatinazo.domain.enums.RuleType;
import com.bancolombia.chocolatinazo.domain.model.Game;
import com.bancolombia.chocolatinazo.domain.port.IGameRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * JPA implementation of IGameRepository.
 * Extends JpaRepository for database operations and implements IGameRepository contract.
 * This maintains the Clean Architecture separation: infrastructure implements domain ports.
 */
@Repository
public interface GameRepository extends JpaRepository<Game, UUID>, IGameRepository {

    // Note: Spring Data JPA allows multiple method names to satisfy interface contracts
    // findByStatus(GameStatus) from JpaRepository queries, while IGameRepository expects Optional
    Optional<Game> findByStatus(GameStatus status);

    Optional<Game> findFirstByStatusOrderByCreatedAtDesc(GameStatus status);

    List<Game> findByRuleType(RuleType ruleType);


}
