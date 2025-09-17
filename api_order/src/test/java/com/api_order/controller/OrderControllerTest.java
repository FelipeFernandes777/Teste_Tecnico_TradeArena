package com.api_order.controller;

import com.api_order.dto.order.CreateOrderRequest;
import com.api_order.dto.order.ResponseOrderDTO;
import com.api_order.dto.item.OrderItemRequest;
import com.api_order.dto.item.OrderItemResponseDTO;
import com.api_order.exceptions.order.InsufficientStockException;
import com.api_order.middlewares.OrderExceptionHandler;
import com.api_order.model.order.OrderStatus;
import com.api_order.services.OrderServices;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class OrderControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private OrderServices orderServices; // Mock da classe concreta

    @InjectMocks
    private OrderController orderController; // Mockito vai injetar o mock

    private UUID tecladoId;
    private UUID mouseId;

    @BeforeEach
    void setup() {
        // Incluindo o ControllerAdvice para capturar exceções e retornar 422
        mockMvc = MockMvcBuilders.standaloneSetup(orderController)
                .setControllerAdvice(new OrderExceptionHandler()) // <- sua classe @ControllerAdvice
                .build();

        tecladoId = UUID.randomUUID();
        mouseId = UUID.randomUUID();
    }


    @Test
    void testCreateOrder_Success() throws Exception {
        ResponseOrderDTO responseDTO = new ResponseOrderDTO(
                UUID.randomUUID(),
                OrderStatus.CREATED,
                new BigDecimal("499.70"),
                List.of(
                        new OrderItemResponseDTO(UUID.randomUUID(), tecladoId, 2, new BigDecimal("199.90")),
                        new OrderItemResponseDTO(UUID.randomUUID(), mouseId, 1, new BigDecimal("99.90"))
                ),
                LocalDateTime.now()
        );

        when(orderServices.createOrder(any(CreateOrderRequest.class)))
                .thenReturn(responseDTO);

        CreateOrderRequest request = new CreateOrderRequest(List.of(
                new OrderItemRequest(tecladoId, 2),
                new OrderItemRequest(mouseId, 1)
        ));

        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.items").isArray())
                .andExpect(jsonPath("$.items.length()").value(2))
                .andExpect(jsonPath("$.total").value(499.70));
    }

    @Test
    void testCreateOrder_InsufficientStock() throws Exception {
        when(orderServices.createOrder(any(CreateOrderRequest.class)))
                .thenThrow(new InsufficientStockException("Insufficient stock for order"));

        CreateOrderRequest request = new CreateOrderRequest(List.of(
                new OrderItemRequest(mouseId, 3)
        ));

        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.message").value("Insufficient stock for order"));
    }
}
