package com.api_order.middlewares;

import com.api_order.exceptions.client.ServiceUnavailableException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@ControllerAdvice
public class ClientExceptionHandler {

    @ExceptionHandler(ServiceUnavailableException.class)
    public ResponseEntity<?> handleServiceUnavailableException(ServiceUnavailableException e) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(
                Map.of(
                        "status", "error",
                        "message", e.getMessage(),
                        "statusCode", HttpStatus.SERVICE_UNAVAILABLE
                )
        );
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleException(RuntimeException e) {
        if (e instanceof ServiceUnavailableException) {
            throw e;
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                Map.of(
                        "status", "error",
                        "message", e.getMessage(),
                        "statusCode", HttpStatus.INTERNAL_SERVER_ERROR
                )
        );
    }
}

