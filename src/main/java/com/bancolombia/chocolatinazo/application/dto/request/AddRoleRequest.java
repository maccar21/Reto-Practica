package com.bancolombia.chocolatinazo.application.dto.request;

import jakarta.validation.constraints.NotBlank;

/**
 * Request body for adding a role to a user.
 */
public class AddRoleRequest {

    @NotBlank(message = "Role name is required")
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

