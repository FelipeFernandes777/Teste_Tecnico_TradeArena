package com.inventory_service.api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CreateProductDTO(
        @NotBlank
        String sku,
        @NotBlank
        String name,
        @NotNull
        @NotNull
        BigDecimal price,
        @Min(0)
        int stock
) {
}
