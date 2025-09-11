package com.inventory_service.api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CreateProductDTO(
        @NotNull
        String sku,
        @NotNull
        @Min(1)
        String name,
        @NotNull
        @Min(1)
        Float price,
        @NotNull
        int stock
) {
}
