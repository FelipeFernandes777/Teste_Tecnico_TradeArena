package com.api_order.dto.item;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.UUID;

public record OrderItemRequest(
    @NotNull(message = "Product ID cannot be null")
    UUID productId,
    @Positive(message = "Quantity must be positive")
    int qty
) {
}
