package com.bancolombia.chocolatinazo.infrastructure.web;

import com.bancolombia.chocolatinazo.application.dto.request.AddRoleRequest;
import com.bancolombia.chocolatinazo.application.dto.response.UserResponse;
import com.bancolombia.chocolatinazo.application.service.AddRoleService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * REST Controller for user management endpoints.
 * Only ADMIN users can access these endpoints.
 * Provides role assignment functionality for existing users.
 */
@RestController
@RequestMapping("/api/users")
public class AddRoleController {

    private final AddRoleService addRoleService;

    public AddRoleController(AddRoleService addRoleService) {
        this.addRoleService = addRoleService;
    }

    /**
     * Add a role to an existing user.
     * Only ADMIN users can invoke this endpoint.
     *
     * @param userId The UUID of the user
     * @param request The request containing the role name
     * @return UserResponse with updated roles
     */
    @PutMapping("/{userId}/add-role")
    public ResponseEntity<UserResponse> addRoleToUser(
            @PathVariable UUID userId,
            @Valid @RequestBody AddRoleRequest request) {

        UserResponse response = addRoleService.addRoleToUser(userId, request.getRoleName());
        return ResponseEntity.ok(response);
    }
}

