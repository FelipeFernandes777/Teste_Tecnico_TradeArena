package com.inventory_service.api.exceptions;

public class DataValueIsMissingException extends RuntimeException {
    public DataValueIsMissingException(String message) {
        super(message);
    }
    public DataValueIsMissingException() {
        super("All fields are required");
    }
}
