package com.bancolombia.chocolatinazo.domain.model;

import com.bancolombia.chocolatinazo.domain.enums.RuleType;
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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RuleType rule;

    @Column(name = "losing_number", nullable = false)
    private Integer losingNumber;

    @Column(name = "total_chocolatinas", nullable = false)
    private Integer totalChocolatinas;

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

    public FinishedGame(UUID id, User loser, RuleType rule, Integer losingNumber, Integer totalChocolatinas,
                        BigDecimal chocolatinaPrice, BigDecimal totalPaid, LocalDateTime finishedAt) {
        this.id = id;
        this.loser = loser;
        this.rule = rule;
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

    public RuleType getRule() {
        return rule;
    }

    public void setRule(RuleType rule) {
        this.rule = rule;
    }

    public Integer getLosingNumber() {
        return losingNumber;
    }

    public void setLosingNumber(Integer losingNumber) {
        this.losingNumber = losingNumber;
    }

    public Integer getTotalChocolatinas() {
        return totalChocolatinas;
    }

    public void setTotalChocolatinas(Integer totalChocolatinas) {
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
