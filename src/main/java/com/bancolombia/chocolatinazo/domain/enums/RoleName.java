package com.bancolombia.chocolatinazo.domain.enums;

/**
 * Defines the available roles in the system for RBAC (Role-Based Access Control).
 * Users can have multiple roles via the user_roles join table.
 *
 * <ul>
 *   <li>{@link #PLAYER} — Can pick chocolatinas in an active game. Default role for new users.</li>
 *   <li>{@link #AUDITOR} — Can view current game records and finished game history.</li>
 *   <li>{@link #ADMIN} — Can create games, calculate losers, update chocolatina price, and assign roles.</li>
 * </ul>
 */
public enum RoleName {
    PLAYER,
    AUDITOR,
    ADMIN
}
