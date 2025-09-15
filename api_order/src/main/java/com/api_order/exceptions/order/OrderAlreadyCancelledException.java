package com.api_order.exceptions.order;

import java.util.UUID;

public class OrderAlreadyCancelledException extends RuntimeException {
    public OrderAlreadyCancelledException(String message) {
        super(message);
    }
    public OrderAlreadyCancelledException(UUID orderId) {
       super("Order with orderId: { " + orderId + " } is already cancelled");
    }
}
