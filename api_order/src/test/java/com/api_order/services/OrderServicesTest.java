package com.api_order.services;

import com.api_order.config.InMemoryOrderRepository;
import com.api_order.config.InventoryClient;
import com.api_order.dto.inventory.ResponseProductDTO;
import com.api_order.dto.item.OrderItemRequest;
import com.api_order.dto.order.CreateOrderRequest;
import com.api_order.dto.order.ResponseOrderDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class OrderServicesTest {

    @Mock
    private InventoryClient inventoryClient;

    private OrderServices orderServices;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        orderServices = new OrderServices(new InMemoryOrderRepository(), inventoryClient);
    }

    @Test
    void createOrder_success() {
        UUID productId = UUID.randomUUID();

        ResponseProductDTO mockProduct = new ResponseProductDTO(
                productId, "SKU123", "Teclado", new BigDecimal("100.00"), 5, LocalDateTime.now()
        );

        when(inventoryClient.verifyIfInventoryApiIsUp()).thenReturn(true);
        when(inventoryClient.getProduct(productId)).thenReturn(mockProduct);

        CreateOrderRequest request = new CreateOrderRequest(
                List.of(new OrderItemRequest(productId, 2))
        );

        ResponseOrderDTO response = orderServices.createOrder(request);

        assertThat(response).isNotNull();
        assertThat(response.items()).hasSize(1);
        assertThat(response.total()).isEqualByComparingTo(new BigDecimal("200.00"));

        verify(inventoryClient, times(1)).updateStock(productId, -2);
    }

    @Test
    void createOrder_insufficientStock_throws() {
        UUID productId = UUID.randomUUID();

        ResponseProductDTO mockProduct = new ResponseProductDTO(
                productId, "SKU123", "Teclado", new BigDecimal("100.00"), 1, LocalDateTime.now()
        );

        when(inventoryClient.verifyIfInventoryApiIsUp()).thenReturn(true);
        when(inventoryClient.getProduct(productId)).thenReturn(mockProduct);

        CreateOrderRequest request = new CreateOrderRequest(
                List.of(new OrderItemRequest(productId, 2))
        );

        org.junit.jupiter.api.Assertions.assertThrows(
                com.api_order.exceptions.order.InsufficientStockException.class,
                () -> orderServices.createOrder(request)
        );

        verify(inventoryClient, never()).updateStock(any(), anyInt());
    }
}