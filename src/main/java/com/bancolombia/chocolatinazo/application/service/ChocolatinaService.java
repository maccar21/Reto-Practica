package com.bancolombia.chocolatinazo.application.service;

import com.bancolombia.chocolatinazo.application.dto.response.ChocolatinaConfigResponse;
import com.bancolombia.chocolatinazo.domain.model.ChocolatinaConfig;
import com.bancolombia.chocolatinazo.domain.model.User;
import com.bancolombia.chocolatinazo.domain.port.IChocolatinaConfigRepository;
import com.bancolombia.chocolatinazo.domain.port.IUserRepository;
import com.bancolombia.chocolatinazo.infrastructure.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Service responsible for chocolatina price configuration.
 * Handles updating and retrieving the current chocolate bar price.
 */
@Service
public class ChocolatinaService {

    private final IChocolatinaConfigRepository chocolatinaConfigRepository;
    private final IUserRepository userRepository;

    public ChocolatinaService(IChocolatinaConfigRepository chocolatinaConfigRepository,
                              IUserRepository userRepository) {
        this.chocolatinaConfigRepository = chocolatinaConfigRepository;
        this.userRepository = userRepository;
    }

    /**
     * Update the current chocolatina price.
     * The admin user who updates the price is stored in the configuration.
     *
     * @param newPrice The new price for a single chocolatina
     * @param adminUserId The UUID of the authenticated ADMIN user (extracted from JWT)
     * @return ChocolatinaConfigResponse with the updated configuration
     * @throws ResourceNotFoundException if admin user not found
     */
    public ChocolatinaConfigResponse updateChocolatinaPrice(BigDecimal newPrice, UUID adminUserId) {
        // Step 1: Find the admin user by ID — throws 404 if user does not exist
        User adminUser = userRepository.findById(adminUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Admin user not found with id: " + adminUserId));

        // Step 2: Get the existing config or create a new one if none exists yet
        ChocolatinaConfig config = chocolatinaConfigRepository.findLatest()
                .orElse(new ChocolatinaConfig());

        // Step 3: Update the price and record which admin made the change
        config.setPrice(newPrice);
        config.setUpdatedBy(adminUser);

        // Step 4: Persist the updated configuration and return the response
        ChocolatinaConfig savedConfig = chocolatinaConfigRepository.save(config);
        return toResponse(savedConfig);
    }

    /**
     * Get the current chocolatina price configuration.
     *
     * @return ChocolatinaConfigResponse with the latest configuration
     * @throws ResourceNotFoundException if no configuration exists
     */
    public ChocolatinaConfigResponse getCurrentConfig() {
        // Retrieve the latest chocolatina price config — throws 404 if no config has been set
        ChocolatinaConfig config = chocolatinaConfigRepository.findLatest()
                .orElseThrow(() -> new ResourceNotFoundException("Chocolatina price not configured yet. An admin must set the price first"));

        return toResponse(config);
    }


    /**
     * Maps a ChocolatinaConfig entity to a ChocolatinaConfigResponse DTO.
     * Includes the admin's ID and username who last updated the price.
     */
    private ChocolatinaConfigResponse toResponse(ChocolatinaConfig config) {
        return new ChocolatinaConfigResponse(
                config.getId(),
                config.getPrice(),
                config.getUpdatedBy().getId(),
                config.getUpdatedBy().getUsername(),
                config.getUpdatedAt()
        );
    }
}
