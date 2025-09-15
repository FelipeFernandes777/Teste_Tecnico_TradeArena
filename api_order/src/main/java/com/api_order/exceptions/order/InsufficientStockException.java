package com.api_order.exceptions.order;

import java.util.UUID;

public class InsufficientStockException extends RuntimeException {
    public InsufficientStockException(String message) {
        super(message);
    }
    public InsufficientStockException(UUID productId, int requested, int available) {
        super("Insufficient stock for product " + productId +
                ": requested " + requested + ", available " + available);
    }
}
