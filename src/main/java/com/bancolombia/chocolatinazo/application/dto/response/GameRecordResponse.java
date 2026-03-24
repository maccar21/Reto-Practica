package com.bancolombia.chocolatinazo.application.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public class GameRecordResponse {

    private UUID id;
    private UUID gameId;
    private UUID userId;
    private String username;
    private int chocolatinaNumber;
    private LocalDateTime pickedAt;

    public GameRecordResponse() {
    }

    public GameRecordResponse(UUID id, UUID gameId, UUID userId, String username, int chocolatinaNumber, LocalDateTime pickedAt) {
        this.id = id;
        this.gameId = gameId;
        this.userId = userId;
        this.username = username;
        this.chocolatinaNumber = chocolatinaNumber;
        this.pickedAt = pickedAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getGameId() {
        return gameId;
    }

    public void setGameId(UUID gameId) {
        this.gameId = gameId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getChocolatinaNumber() {
        return chocolatinaNumber;
    }

    public void setChocolatinaNumber(int chocolatinaNumber) {
        this.chocolatinaNumber = chocolatinaNumber;
    }

    public LocalDateTime getPickedAt() {
        return pickedAt;
    }

    public void setPickedAt(LocalDateTime pickedAt) {
        this.pickedAt = pickedAt;
    }
}

