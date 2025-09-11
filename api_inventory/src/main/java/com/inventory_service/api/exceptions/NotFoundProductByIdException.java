package com.inventory_service.api.exceptions;

public class NotFoundProductByIdException extends RuntimeException {
    public NotFoundProductByIdException(String message) {
        super(message);
    }
    public NotFoundProductByIdException() {
        super("Product not found");
    }
}
