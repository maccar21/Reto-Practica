package com.bancolombia.chocolatinazo.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * Request body for creating a new game.
 */
public class CreateGameRequest {

    @NotBlank(message = "Rule is required")
    @Pattern(regexp = "^(MIN|MAX)$", message = "Rule must be either MIN or MAX")
    private String rule;

    public CreateGameRequest() {
    }

    public CreateGameRequest(String rule) {
        this.rule = rule;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }
}

