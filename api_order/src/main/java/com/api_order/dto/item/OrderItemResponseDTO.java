package com.api_order.dto.item;

import com.api_order.model.orderItem.OrderItemModel;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderItemResponseDTO(
        UUID id,
        UUID productId,
        int qty,
        BigDecimal unitPrice
) {
    public OrderItemResponseDTO(OrderItemModel item) {
        this(item.getId(), item.getProduct_id(), item.getQty(), item.getUnitPrice());
    }
}