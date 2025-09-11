package com.inventory_service.api.exceptions;

public class FailedToCreateANewProductException extends RuntimeException {
    public FailedToCreateANewProductException(String message) {
        super(message);
    }
    public FailedToCreateANewProductException() {
        super("Failed to create new product");
    }
}
