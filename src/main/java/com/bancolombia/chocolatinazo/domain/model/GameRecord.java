package com.bancolombia.chocolatinazo.domain.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * JPA entity representing a single player's chocolatina pick within a game.
 * Maps to the "game_records" table. Each record links a player (User) to a game (Game)
 * with their randomly generated chocolatina number (1-320).
 *
 * <p>Business rules enforced at service layer:</p>
 * <ul>
 *   <li>Each player can only pick one chocolatina per game.</li>
 *   <li>Chocolatina numbers cannot be repeated within the same game.</li>
 *   <li>All records are deleted when the game is finished to prepare for the next round.</li>
 * </ul>
 */
@Entity
@Table(name = "game_records")
public class GameRecord{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "chocolatina_number", nullable = false)
    private int chocolatinaNumber;

    @Column(name = "picked_at", nullable = false, updatable = false)
    private LocalDateTime pickedAt;

    public GameRecord() {}

    public GameRecord(UUID id, Game game, User user, int chocolatinaNumber, LocalDateTime pickedAt) {
        this.id = id;
        this.game = game;
        this.user = user;
        this.chocolatinaNumber = chocolatinaNumber;
        this.pickedAt = pickedAt;
    }

    /** Automatically sets the pick timestamp before the entity is first persisted. */
    @PrePersist
    protected void onCreate() {
        this.pickedAt = LocalDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
