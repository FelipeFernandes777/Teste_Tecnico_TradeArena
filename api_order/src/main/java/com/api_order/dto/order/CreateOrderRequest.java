package com.api_order.dto.order;

import com.api_order.dto.item.OrderItemRequest;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CreateOrderRequest(
        @NotNull(message = "Items cannot be null")
        List<OrderItemRequest> items
) {
}
