package com.bancolombia.chocolatinazo.infrastructure.web;

import com.bancolombia.chocolatinazo.application.dto.request.AddRoleRequest;
import com.bancolombia.chocolatinazo.application.dto.response.UserResponse;
import com.bancolombia.chocolatinazo.application.service.UserService;
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
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Add a role to an existing user.
     * Only ADMIN users can invoke this endpoint.
     *
     * @param userId The UUID of the user
     * @param request The request containing the role name
     * @return UserResponse with updated roles
     */
    @PutMapping("/{userId}/role")
    public ResponseEntity<UserResponse> addRoleToUser(
            @PathVariable UUID userId,
            @Valid @RequestBody AddRoleRequest request) {

        UserResponse response = userService.addRoleToUser(userId, request.getRoleName());
        return ResponseEntity.ok(response);
    }
}

