package com.bancolombia.chocolatinazo.infrastructure.repository;

import com.bancolombia.chocolatinazo.domain.enums.GameStatus;
import com.bancolombia.chocolatinazo.domain.enums.RuleType;
import com.bancolombia.chocolatinazo.domain.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface GameRepository extends JpaRepository<Game, UUID> {

    List<Game> findByStatus(GameStatus status);

    Optional<Game> findFirstByStatusOrderByCreatedAtDesc(GameStatus status);

    List<Game> findByRuleType(RuleType ruleType);


}
