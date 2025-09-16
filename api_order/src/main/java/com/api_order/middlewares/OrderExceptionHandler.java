package com.api_order.middlewares;

import com.api_order.exceptions.order.InsufficientStockException;
import com.api_order.exceptions.order.OrderAlreadyCancelledException;
import com.api_order.exceptions.order.OrderNotFoundException;
import com.api_order.exceptions.order.ProductNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@ControllerAdvice
public class OrderExceptionHandler {

    @ExceptionHandler(InsufficientStockException.class)
    public ResponseEntity<?> handleInsufficientStockException(InsufficientStockException e) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(
                Map.of(
                        "status", "alert",
                        "message", e.getMessage(),
                        "statusCode", HttpStatus.UNPROCESSABLE_ENTITY
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

    @ExceptionHandler(OrderAlreadyCancelledException.class)
    public ResponseEntity<?> handleOrderAlreadyCancelledException(OrderAlreadyCancelledException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                Map.of(
                        "status", "alert",
                        "message", e.getMessage(),
                        "statusCode", HttpStatus.CONFLICT
                )
        );
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleGenericRuntime(RuntimeException ex) {
        if (ex instanceof InsufficientStockException ||
                ex instanceof OrderAlreadyCancelledException ||
                ex instanceof ProductNotFoundException ||
                ex instanceof OrderNotFoundException) {
            throw ex;
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                Map.of(
                        "statusCode", HttpStatus.INTERNAL_SERVER_ERROR,
                        "status", "error",
                        "message", "An unexpected error occurred: " + ex.getMessage()
                )
        );
    }
}
