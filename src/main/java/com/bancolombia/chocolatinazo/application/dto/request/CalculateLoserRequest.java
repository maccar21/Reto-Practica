package com.bancolombia.chocolatinazo.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

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

