package com.api_order.config;

import com.api_order.dto.inventory.ResponseProductDTO;
import com.api_order.dto.item.OrderItemRequest;
import com.api_order.dto.order.CreateOrderRequest;
import com.api_order.services.OrderServices;
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

class InventoryClientTest {

    @Mock
    private InventoryClient inventoryClient;

    private OrderServices orderServices;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        // Usando um repositório in-memory fake
        orderServices = new OrderServices(new InMemoryOrderRepository(), inventoryClient);
    }

    @Test
    void createOrder_withMockedInventoryClient_success() {
        UUID productId = UUID.randomUUID();

        // Mock do retorno do inventário
        ResponseProductDTO mockProduct = new ResponseProductDTO(
                productId, "SKU123", "Teclado", new BigDecimal("100.00"), 5, LocalDateTime.now()
        );

        when(inventoryClient.verifyIfInventoryApiIsUp()).thenReturn(true);
        when(inventoryClient.getProduct(productId)).thenReturn(mockProduct);

        // Criação do pedido
        CreateOrderRequest request = new CreateOrderRequest(
                List.of(new OrderItemRequest(productId, 2))
        );

        var response = orderServices.createOrder(request);

        // Verificações
        assertThat(response).isNotNull();
        assertThat(response.items()).hasSize(1);

        // Verifica se o estoque foi atualizado
        verify(inventoryClient, times(1)).updateStock(productId, -2);
    }
}
