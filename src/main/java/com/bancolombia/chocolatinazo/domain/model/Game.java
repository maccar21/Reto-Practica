package com.bancolombia.chocolatinazo.domain.model;

import com.bancolombia.chocolatinazo.domain.enums.GameStatus;
import com.bancolombia.chocolatinazo.domain.enums.RuleType;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;


/**
 * JPA entity representing a chocolatinazo game session.
 * Maps to the "games" table. Each game has a status (ACTIVE or FINISHED)
 * and a rule type (MIN or MAX) that determines how the loser is calculated.
 * Only one ACTIVE game can exist at a time — enforced at the service layer.
 * The createdAt timestamp is automatically set on persist.
 */
@Entity
@Table(name = "games")
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GameStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "rule_type", nullable = false)
    private RuleType ruleType;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public Game() {
    }

    public Game(UUID id, GameStatus status, RuleType ruleType, LocalDateTime createdAt) {
        this.id = id;
        this.status = status;
        this.ruleType = ruleType;
        this.createdAt = createdAt;
    }

    /** Automatically sets the creation timestamp before the entity is first persisted. */
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public GameStatus getStatus() {
        return status;
    }

    public void setStatus(GameStatus status) {
        this.status = status;
    }

    public RuleType getRuleType() {
        return ruleType;
    }

    public void setRuleType(RuleType ruleType) {
        this.ruleType = ruleType;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}

