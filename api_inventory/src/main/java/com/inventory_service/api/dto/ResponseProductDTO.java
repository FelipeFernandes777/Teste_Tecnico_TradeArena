package com.inventory_service.api.dto;

import com.inventory_service.api.model.ProductModel;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record ResponseProductDTO(
        UUID id,
        String name,
        String sku,
        BigDecimal price,
        int stock,
        LocalDateTime created_at
) {
    public ResponseProductDTO(ProductModel raw){
        this(raw.getId(), raw.getName(),raw.getSku(),raw.getPrice(),raw.getStock(),raw.getCreatedAt());
    }
}
