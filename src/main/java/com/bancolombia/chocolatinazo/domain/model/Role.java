package com.bancolombia.chocolatinazo.domain.model;

import com.bancolombia.chocolatinazo.domain.enums.RoleName;
import jakarta.persistence.*;

import java.util.UUID;

/**
 * JPA entity representing a role in the system.
 * Maps to the "roles" table. Roles are used for RBAC authorization.
 * Each role has a unique name defined by {@link RoleName} (PLAYER, AUDITOR, ADMIN).
 * Users are associated with roles through a ManyToMany relationship via the user_roles join table.
 */
@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private RoleName name;

    public Role() {
    }

    public Role(UUID id) {
        this.id = id;
    }

    public Role(UUID id, RoleName name) {
        this.id = id;
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public RoleName getName() {
        return name;
    }

    public void setName(RoleName name) {
        this.name = name;
    }
}
