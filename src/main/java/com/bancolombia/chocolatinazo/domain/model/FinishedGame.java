package com.bancolombia.chocolatinazo.domain.model;

import com.bancolombia.chocolatinazo.domain.enums.RuleType;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;


/**
 * JPA entity representing the result of a completed chocolatinazo game.
 * Maps to the "finished_games" table. Stores a snapshot of all relevant data
 * at the moment the game was finished, including:
 * <ul>
 *   <li>The loser (User) and their losing chocolatina number.</li>
 *   <li>The rule applied (MIN or MAX) to determine the loser.</li>
 *   <li>The chocolatina price at the time of calculation (snapshot, not a reference).</li>
 *   <li>The total number of chocolatinas played and the total amount the loser must pay.</li>
 * </ul>
 *
 * <p>Design decision: the loser's username is obtained via the User relationship (normalized)
 * rather than being denormalized into a separate column.</p>
 */
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

    @Enumerated(EnumType.STRING)
    @Column(name = "rule_type", nullable = false)
    private RuleType ruleType;

    @Column(name = "finished_at", nullable = false, updatable = false)
    private LocalDateTime finishedAt;

    public FinishedGame() {
    }

    public FinishedGame(UUID id) {
        this.id = id;
    }

    public FinishedGame(UUID id, Game game, User loser, int losingNumber, int totalChocolatinas,
                        BigDecimal chocolatinaPrice, BigDecimal totalPaid, RuleType ruleType, LocalDateTime finishedAt) {
        this.id = id;
        this.game = game;
        this.loser = loser;
        this.losingNumber = losingNumber;
        this.totalChocolatinas = totalChocolatinas;
        this.chocolatinaPrice = chocolatinaPrice;
        this.totalPaid = totalPaid;
        this.ruleType = ruleType;
        this.finishedAt = finishedAt;
    }

    /** Automatically sets the finished timestamp before the entity is first persisted. */
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

    public RuleType getRuleType() {
        return ruleType;
    }

    public void setRuleType(RuleType ruleType) {
        this.ruleType = ruleType;
    }

    public LocalDateTime getFinishedAt() {
        return finishedAt;
    }

    public void setFinishedAt(LocalDateTime finishedAt) {
        this.finishedAt = finishedAt;
    }
}
