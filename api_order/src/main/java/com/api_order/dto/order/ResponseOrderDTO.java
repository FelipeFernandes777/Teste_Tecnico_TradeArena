package com.api_order.dto.order;

import com.api_order.model.order.OrderModel;
import com.api_order.model.order.OrderStatus;
import com.api_order.model.orderItem.OrderItemModel;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record ResponseOrderDTO(
            UUID id,
            OrderStatus status,
            BigDecimal total,
            List<OrderItemModel> items,
            LocalDateTime createdAt
        ) {
    public ResponseOrderDTO(OrderModel raw){
        this(raw.getId(),raw.getStatus(),raw.getTotal(),raw.getItems(),raw.getCreatedAt());
    }
}
