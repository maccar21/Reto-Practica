package com.bancolombia.chocolatinazo.domain.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

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
    private Integer chocolatinaNumber;

    @Column(name = "picked_at", nullable = false, updatable = false)
    private LocalDateTime pickedAt;

    public GameRecord() {}

    public GameRecord(UUID id, Game game, User user, Integer chocolatinaNumber, LocalDateTime pickedAt) {
        this.id = id;
        this.game = game;
        this.user = user;
        this.chocolatinaNumber = chocolatinaNumber;
        this.pickedAt = pickedAt;
    }

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

    public Integer getChocolatinaNumber() {
        return chocolatinaNumber;
    }

    public void setChocolatinaNumber(Integer chocolatinaNumber) {
        this.chocolatinaNumber = chocolatinaNumber;
    }

    public LocalDateTime getPickedAt() {
        return pickedAt;
    }

    public void setPickedAt(LocalDateTime pickedAt) {
        this.pickedAt = pickedAt;
    }
}
