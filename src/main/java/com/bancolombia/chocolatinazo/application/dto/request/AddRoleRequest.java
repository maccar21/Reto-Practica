package com.bancolombia.chocolatinazo.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * Request DTO for adding a role to an existing user.
 * Only ADMIN users can invoke this operation.
 * Valid role names are: PLAYER, AUDITOR, ADMIN.
 */
public class AddRoleRequest {

    @NotBlank(message = "Role name is required")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "Role name must contain only letters, no numbers or special characters")
    private String roleName;

    public AddRoleRequest() {
    }

    public AddRoleRequest(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}

