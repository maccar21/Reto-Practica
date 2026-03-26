package com.bancolombia.chocolatinazo.domain.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;


/**
 * JPA entity representing the chocolatina price configuration.
 * Maps to the "chocolatina_config" table. Stores the current price of a chocolatina
 * and tracks which admin user last updated it.
 *
 * <p>The updatedAt timestamp is automatically set on both persist and update
 * via {@link jakarta.persistence.PrePersist} and {@link jakarta.persistence.PreUpdate}.</p>
 *
 * <p>When calculating the loser, the price is snapshotted into the FinishedGame record
 * to preserve historical accuracy.</p>
 */
@Entity
@Table(name = "chocolatina_config")
public class ChocolatinaConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private BigDecimal price;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "updated_by", nullable = false)
    private User updatedBy;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public ChocolatinaConfig() {
    }

    public ChocolatinaConfig(UUID id) {
        this.id = id;
    }

    public ChocolatinaConfig(UUID id, BigDecimal price, User updatedBy, LocalDateTime updatedAt) {
        this.id = id;
        this.price = price;
        this.updatedBy = updatedBy;
        this.updatedAt = updatedAt;
    }

    /** Automatically sets the update timestamp on both insert and update operations. */
    @PrePersist
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
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

    public User getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(User updatedBy) {
        this.updatedBy = updatedBy;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
