package com.inventory_service.api.dto;

import com.inventory_service.api.model.ProductModel;

import java.time.LocalDateTime;

public record ResponseProductDTO(
        String id,
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
