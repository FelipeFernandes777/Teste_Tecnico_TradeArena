package com.api_order.dto.order;

import java.util.UUID;

public record StockDetail(
        UUID productId,
        int requested,
        int available
) {
}
