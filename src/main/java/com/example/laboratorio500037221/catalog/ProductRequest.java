package com.example.laboratorio500037221.catalog;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ProductRequest(
        @NotBlank String sku,
        @NotBlank String name,
        @Min(0) int stock,
        @NotNull @DecimalMin("0.01") BigDecimal unitPrice
) {
}
