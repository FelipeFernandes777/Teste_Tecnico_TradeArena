package com.api_order.dto.inventory;

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
}