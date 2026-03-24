package com.bancolombia.chocolatinazo.application.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class ChocolatinaConfigResponse {

    private UUID id;
    private BigDecimal price;
    private UUID updatedById;
    private String updatedByUsername;
    private LocalDateTime updatedAt;

    public ChocolatinaConfigResponse() {
    }

    public ChocolatinaConfigResponse(UUID id, BigDecimal price, UUID updatedById, String updatedByUsername, LocalDateTime updatedAt) {
        this.id = id;
        this.price = price;
        this.updatedById = updatedById;
        this.updatedByUsername = updatedByUsername;
        this.updatedAt = updatedAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public UUID getUpdatedById() {
        return updatedById;
    }

    public void setUpdatedById(UUID updatedById) {
        this.updatedById = updatedById;
    }

    public String getUpdatedByUsername() {
        return updatedByUsername;
    }

    public void setUpdatedByUsername(String updatedByUsername) {
        this.updatedByUsername = updatedByUsername;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}

