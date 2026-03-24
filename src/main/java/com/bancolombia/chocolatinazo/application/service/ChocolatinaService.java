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
        User adminUser = userRepository.findById(adminUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Admin user not found with id: " + adminUserId));

        // If a config already exists, update it. Otherwise create a new one.
        ChocolatinaConfig config = chocolatinaConfigRepository.findLatest()
                .orElse(new ChocolatinaConfig());

        config.setPrice(newPrice);
        config.setUpdatedBy(adminUser);

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
        ChocolatinaConfig config = chocolatinaConfigRepository.findLatest()
                .orElseThrow(() -> new ResourceNotFoundException("Chocolatina price not configured yet. An admin must set the price first"));

        return toResponse(config);
    }

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

