package com.inventory_service.api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateProductDTO(
        @NotBlank
        String sku,
        @NotBlank
        String name,
        @NotNull
        @Min(1)
        Float price,
        @Min(0)
        int stock
) {
}
