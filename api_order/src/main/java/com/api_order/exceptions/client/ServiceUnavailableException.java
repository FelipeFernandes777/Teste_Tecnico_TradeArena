package com.api_order.exceptions.order;

public class ServiceUnavailableException extends RuntimeException {
    public ServiceUnavailableException(String message) {
        super(message);
    }

    public ServiceUnavailableException() {
        super("Inventory service is unavailable");
    }
}
