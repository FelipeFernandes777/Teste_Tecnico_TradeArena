package com.api_order.controller;

import com.api_order.config.InventoryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/test")
public class TController {
    private final InventoryClient inventoryClient;

    public TController(InventoryClient inventoryClient) {
        this.inventoryClient = inventoryClient;
    }

    @GetMapping("/inventory-status")
    public Map<String, Object> checkInventoryStatus() {
        boolean isUp = inventoryClient.verifyIfInventoryApiIsUp();
        return Map.of(
                "inventoryApi", isUp ? "UP" : "DOWN",
                "timestamp", LocalDateTime.now().toString()
        );
    }
}
