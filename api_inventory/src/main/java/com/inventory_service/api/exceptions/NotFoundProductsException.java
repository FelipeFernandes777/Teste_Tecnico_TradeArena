package com.inventory_service.api.exceptions;

public class NotFoundProductsException extends RuntimeException {
    public NotFoundProductsException(String message) {
        super(message);
    }
    public NotFoundProductsException() {
        super("Products not found");
    }
}
