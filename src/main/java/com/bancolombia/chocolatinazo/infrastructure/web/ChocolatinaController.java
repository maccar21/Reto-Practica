package com.bancolombia.chocolatinazo.infrastructure.web;

import com.bancolombia.chocolatinazo.application.dto.request.UpdateChocolatinaValueRequest;
import com.bancolombia.chocolatinazo.application.dto.response.ChocolatinaConfigResponse;
import com.bancolombia.chocolatinazo.application.service.ChocolatinaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * REST Controller for chocolatina price configuration endpoints.
 */
@RestController
@RequestMapping("/api/chocolatina")
public class ChocolatinaController {

    private final ChocolatinaService chocolatinaService;

    public ChocolatinaController(ChocolatinaService chocolatinaService) {
        this.chocolatinaService = chocolatinaService;
    }

    /**
     * Update the chocolatina price.
     * Only ADMIN users can invoke this endpoint.
     * The admin userId is extracted from the JWT token.
     *
     * @param request The request containing the new price
     * @param authentication The authentication object containing the admin user ID
     * @return ChocolatinaConfigResponse with the updated configuration
     */
    @PutMapping("/value")
    public ResponseEntity<ChocolatinaConfigResponse> updateChocolatinaPrice(
            @Valid @RequestBody UpdateChocolatinaValueRequest request,
            Authentication authentication) {
        UUID adminUserId = UUID.fromString(authentication.getPrincipal().toString());
        ChocolatinaConfigResponse response = chocolatinaService.updateChocolatinaPrice(request.getPrice(), adminUserId);
        return ResponseEntity.ok(response);
    }

    /**
     * Get the current chocolatina price.
     * Any authenticated user can view the current price.
     *
     * @return ChocolatinaConfigResponse with the latest price configuration
     */
    @GetMapping("/value")
    public ResponseEntity<ChocolatinaConfigResponse> getCurrentConfig() {
        ChocolatinaConfigResponse response = chocolatinaService.getCurrentConfig();
        return ResponseEntity.ok(response);
    }
}

