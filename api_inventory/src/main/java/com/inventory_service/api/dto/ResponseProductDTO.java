package com.inventory_service.api.dto;

import com.inventory_service.api.model.ProductModel;

import java.time.LocalDateTime;
import java.util.UUID;

public record ResponseProductDTO(
        UUID id,
        String name,
        String sku,
        Float price,
        int stock,
        LocalDateTime created_at
) {
    public ResponseProductDTO(ProductModel raw){
        this(raw.getId(), raw.getName(),raw.getSku(),raw.getPrice(),raw.getStock(),raw.getCreatedAt());
    }
}
