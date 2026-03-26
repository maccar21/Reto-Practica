package com.bancolombia.chocolatinazo.domain.enums;

/**
 * Defines the rule used to determine the loser of a chocolatinazo game round.
 * The rule is specified when calculating the loser and is stored in the finished game record.
 *
 * <ul>
 *   <li>{@link #MIN} — The player with the lowest chocolatina number loses.</li>
 *   <li>{@link #MAX} — The player with the highest chocolatina number loses.</li>
 * </ul>
 */
public enum RuleType {
    MIN,
    MAX
}
