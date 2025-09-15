package com.api_order.middlewares;

import com.api_order.exceptions.order.InsufficientStockException;
import com.api_order.exceptions.order.OrderNotFoundException;
import com.api_order.exceptions.order.ProductNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@ControllerAdvice
public class OrderExeceptionHandler {

    @ExceptionHandler(InsufficientStockException.class)
    public ResponseEntity<?> handleInsufficientStockException(InsufficientStockException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                Map.of(
                        "status", "alert",
                        "message", e.getMessage(),
                        "statusCode", HttpStatus.BAD_REQUEST
                )
        );
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<?> handleOrderNotFoundException(OrderNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                Map.of(
                        "status", "alert",
                        "message", e.getMessage(),
                        "statusCode", HttpStatus.NOT_FOUND
                )
        );
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<?> handleProductNotFoundException(ProductNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                Map.of(
                        "status", "alert",
                        "message", e.getMessage(),
                        "statusCode", HttpStatus.NOT_FOUND
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

