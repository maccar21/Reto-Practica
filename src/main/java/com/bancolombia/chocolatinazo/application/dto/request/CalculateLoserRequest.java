package com.bancolombia.chocolatinazo.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * Request DTO for calculating the loser of the current game.
 * Contains the rule to determine the loser: "MIN" (lowest number loses) or "MAX" (highest number loses).
 */
public class CalculateLoserRequest {

    @NotBlank(message = "Rule is required")
    @Pattern(regexp = "^(MIN|MAX)$", message = "Rule must be either MIN or MAX")
    private String rule;

    public CalculateLoserRequest() {
    }

    public CalculateLoserRequest(String rule) {
        this.rule = rule;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }
}

