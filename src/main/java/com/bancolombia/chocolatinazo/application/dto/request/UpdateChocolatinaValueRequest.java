package com.bancolombia.chocolatinazo.application.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

/**
 * Request DTO for updating the chocolatina price.
 * Only ADMIN users can invoke this operation.
 * The price must be a positive decimal value (minimum 0.01).
 */
public class UpdateChocolatinaValueRequest {

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    private BigDecimal price;

    public UpdateChocolatinaValueRequest() {
    }

    public UpdateChocolatinaValueRequest(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}

