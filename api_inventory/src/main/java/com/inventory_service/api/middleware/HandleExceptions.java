package com.inventory_service.api.middleware;

import com.inventory_service.api.exceptions.DataValueIsMissingException;
import com.inventory_service.api.exceptions.FailedToCreateANewProductException;
import com.inventory_service.api.exceptions.NotFoundProductByIdException;
import com.inventory_service.api.exceptions.NotFoundProductsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@ControllerAdvice
public class HandleExceptions {

    @ExceptionHandler(DataValueIsMissingException.class)
    public ResponseEntity<?> handleDataValueIsMissingException(DataValueIsMissingException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                Map.of(
                        "statusCode", HttpStatus.BAD_REQUEST,
                        "status", "error",
                        "message", ex.getMessage()
                )
        );
    }

    @ExceptionHandler(FailedToCreateANewProductException.class)
    public ResponseEntity<?> handleFailedToCreateANewProductException(FailedToCreateANewProductException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                Map.of(
                        "statusCode", HttpStatus.BAD_REQUEST,
                        "status", "error",
                        "message", ex.getMessage()
                )
        );
    }

    @ExceptionHandler(NotFoundProductByIdException.class)
    public ResponseEntity<?> handleNotFoundProductByIdExceptionException(NotFoundProductByIdException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                Map.of(
                        "statusCode", HttpStatus.NOT_FOUND,
                        "status", "error",
                        "message", ex.getMessage()
                )
        );
    }

    @ExceptionHandler(NotFoundProductsException.class)
    public ResponseEntity<?> handleNotFoundProductsException(NotFoundProductsException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                Map.of(
                        "statusCode", HttpStatus.NOT_FOUND,
                        "status", "error",
                        "message", ex.getMessage()
                )
        );
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleAnotherNotTreatedException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                Map.of(
                        "statusCode", HttpStatus.INTERNAL_SERVER_ERROR,
                        "status", "error",
                        "message", "An unexpected error occurred: " + ex.getMessage()
                )
        );
    }
}
