package com.api_order.controller;

import com.api_order.dto.order.CreateOrderRequest;
import com.api_order.dto.order.ResponseOrderDTO;
import com.api_order.services.IOrderServices;
import com.api_order.services.OrderServices;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderServices orderServices;

    public OrderController(OrderServices services) {
        this.orderServices = services;
    }

    @PostMapping
    public ResponseEntity<ResponseOrderDTO> createOrder(
            @Valid @RequestBody CreateOrderRequest request) {
        ResponseOrderDTO response = orderServices.createOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseOrderDTO> getOrderById(@PathVariable UUID id) {
        ResponseOrderDTO response = orderServices.getOrderForId(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<ResponseOrderDTO>> getOrders(Pageable pageable) {
        Page<ResponseOrderDTO> orders = orderServices.getOrders(pageable);
        return ResponseEntity.ok(orders);
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelOrder(@PathVariable UUID id) {
        orderServices.cancelOrder(id);
        return ResponseEntity.noContent().build();
    }
}
