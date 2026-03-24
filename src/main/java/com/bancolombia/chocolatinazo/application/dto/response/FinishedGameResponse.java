package com.bancolombia.chocolatinazo.application.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class FinishedGameResponse {

    private UUID id;
    private UUID gameId;
    private UUID loserUserId;
    private String loserUsername;
    private int losingNumber;
    private int totalChocolatinas;
    private BigDecimal chocolatinaPrice;
    private BigDecimal totalPaid;
    private LocalDateTime finishedAt;

    public FinishedGameResponse() {
    }

    public FinishedGameResponse(UUID id, UUID gameId, UUID loserUserId, String loserUsername, int losingNumber, int totalChocolatinas, BigDecimal chocolatinaPrice, BigDecimal totalPaid, LocalDateTime finishedAt) {
        this.id = id;
        this.gameId = gameId;
        this.loserUserId = loserUserId;
        this.loserUsername = loserUsername;
        this.losingNumber = losingNumber;
        this.totalChocolatinas = totalChocolatinas;
        this.chocolatinaPrice = chocolatinaPrice;
        this.totalPaid = totalPaid;
        this.finishedAt = finishedAt;
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

    public UUID getLoserUserId() {
        return loserUserId;
    }

    public void setLoserUserId(UUID loserUserId) {
        this.loserUserId = loserUserId;
    }

    public String getLoserUsername() {
        return loserUsername;
    }

    public void setLoserUsername(String loserUsername) {
        this.loserUsername = loserUsername;
    }

    public int getLosingNumber() {
        return losingNumber;
    }

    public void setLosingNumber(int losingNumber) {
        this.losingNumber = losingNumber;
    }

    public int getTotalChocolatinas() {
        return totalChocolatinas;
    }

    public void setTotalChocolatinas(int totalChocolatinas) {
        this.totalChocolatinas = totalChocolatinas;
    }

    public BigDecimal getChocolatinaPrice() {
        return chocolatinaPrice;
    }

    public void setChocolatinaPrice(BigDecimal chocolatinaPrice) {
        this.chocolatinaPrice = chocolatinaPrice;
    }

    public BigDecimal getTotalPaid() {
        return totalPaid;
    }

    public void setTotalPaid(BigDecimal totalPaid) {
        this.totalPaid = totalPaid;
    }

    public LocalDateTime getFinishedAt() {
        return finishedAt;
    }

    public void setFinishedAt(LocalDateTime finishedAt) {
        this.finishedAt = finishedAt;
    }
}

