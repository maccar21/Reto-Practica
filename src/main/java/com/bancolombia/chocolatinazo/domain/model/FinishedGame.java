package com.bancolombia.chocolatinazo.domain.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;


@Entity
@Table(name = "finished_games")
public class FinishedGame {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "loser_user_id", nullable = false)
    private User loser;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;

    @Column(name = "losing_number", nullable = false)
    private int losingNumber;

    @Column(name = "total_chocolatinas", nullable = false)
    private int totalChocolatinas;

    @Column(name = "chocolatina_price", nullable = false)
    private BigDecimal chocolatinaPrice;

    @Column(name = "total_paid", nullable = false)
    private BigDecimal totalPaid;

    @Column(name = "finished_at", nullable = false, updatable = false)
    private LocalDateTime finishedAt;

    public FinishedGame() {
    }

    public FinishedGame(UUID id) {
        this.id = id;
    }

    public FinishedGame(UUID id,Game game, User loser, int losingNumber, int totalChocolatinas,
                        BigDecimal chocolatinaPrice, BigDecimal totalPaid, LocalDateTime finishedAt) {
        this.id = id;
        this.game = game;
        this.loser = loser;
        this.losingNumber = losingNumber;
        this.totalChocolatinas = totalChocolatinas;
        this.chocolatinaPrice = chocolatinaPrice;
        this.totalPaid = totalPaid;
        this.finishedAt = finishedAt;
    }

    @PrePersist
    protected void onCreate() {
        this.finishedAt = LocalDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public User getLoser() {
        return loser;
    }

    public void setLoser(User loser) {
        this.loser = loser;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
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
