package com.api_order.exceptions.order;

import java.util.UUID;

public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(String message) {
        super(message);
    }
    public OrderNotFoundException(UUID order_id) {
        super("Order not found with order_id: " + order_id);
    }

    public OrderNotFoundException() {
        super("Order not found");
    }
}
