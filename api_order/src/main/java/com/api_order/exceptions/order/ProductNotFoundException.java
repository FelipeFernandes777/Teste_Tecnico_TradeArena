package com.api_order.exceptions.order;

import java.util.UUID;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(String message) {
        super(message);
    }
    public ProductNotFoundException(UUID productId) {
        super("Product not found: " + productId);
    }
    public ProductNotFoundException() {
        super("Product not found");
    }
}
