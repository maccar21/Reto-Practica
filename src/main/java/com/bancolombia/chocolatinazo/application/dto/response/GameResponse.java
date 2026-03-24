package com.bancolombia.chocolatinazo.application.dto.response;

import com.bancolombia.chocolatinazo.domain.enums.GameStatus;
import com.bancolombia.chocolatinazo.domain.enums.RuleType;

import java.time.LocalDateTime;
import java.util.UUID;

public class GameResponse {

    private UUID id;
    private GameStatus status;
    private RuleType ruleType;
    private LocalDateTime createdAt;

    public GameResponse() {
    }

    public GameResponse(UUID id, GameStatus status, RuleType ruleType, LocalDateTime createdAt) {
        this.id = id;
        this.status = status;
        this.ruleType = ruleType;
        this.createdAt = createdAt;
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

