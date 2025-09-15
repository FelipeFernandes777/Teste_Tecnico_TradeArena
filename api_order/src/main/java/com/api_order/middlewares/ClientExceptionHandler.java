package com.api_order.middlewares;

import com.api_order.exceptions.client.ServiceUnavailableException;
import com.api_order.exceptions.order.InsufficientStockException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@ControllerAdvice
public class ClientExceptionHandler {

    @ExceptionHandler(ServiceUnavailableException.class)
    public ResponseEntity<?> handleInsufficientStockException(ServiceUnavailableException e) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(
                Map.of(
                        "status", "error",
                        "message", e.getMessage(),
                        "statusCode", HttpStatus.SERVICE_UNAVAILABLE
                )
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                Map.of(
                        "status", "error",
                        "message", e.getMessage(),
                        "statusCode", HttpStatus.INTERNAL_SERVER_ERROR
                )
        );
    }
}

