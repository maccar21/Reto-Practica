package com.bancolombia.chocolatinazo.domain.enums;

/**
 * Represents the possible states of a chocolatinazo game.
 * Only one game can be ACTIVE at any given time.
 *
 * <ul>
 *   <li>{@link #ACTIVE} — Game is currently in progress; players can pick chocolatinas.</li>
 *   <li>{@link #FINISHED} — Game has ended; the loser has been determined and recorded.</li>
 * </ul>
 */
public enum GameStatus {
    ACTIVE,
    FINISHED
}
