package com.api_order.repository;

import com.api_order.model.order.OrderModel;
import com.api_order.model.order.OrderStatus;
import com.api_order.model.orderItem.OrderItemModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Test
    void testSaveAndFindOrderWithItems() {
        // Cria pedido
        OrderModel order = new OrderModel();
        order.setStatus(OrderStatus.CREATED);
        order.setTotal(new BigDecimal("100.00"));
        order.setCreatedAt(LocalDateTime.now());

        // Cria item e associa ao pedido
        OrderItemModel item = new OrderItemModel();
        item.setProduct_id(UUID.randomUUID());
        item.setQty(1);
        item.setUnitPrice(new BigDecimal("100.00"));
        item.setOrder(order); // associação

        // Associa lista de itens ao pedido
        order.setItems(List.of(item));

        // Salva pedido (itens serão persistidos automaticamente pelo Cascade)
        OrderModel savedOrder = orderRepository.save(order);

        // Valida campos do pedido
        assertThat(savedOrder.getId()).isNotNull();
        assertThat(savedOrder.getItems()).hasSize(1);
        assertThat(savedOrder.getItems().get(0).getProduct_id()).isEqualTo(item.getProduct_id());

        // Busca pedido no banco
        var foundOrder = orderRepository.findById(savedOrder.getId());
        assertThat(foundOrder).isPresent();
        assertThat(foundOrder.get().getTotal()).isEqualByComparingTo(new BigDecimal("100.00"));
        assertThat(foundOrder.get().getItems()).hasSize(1);
    }
}
