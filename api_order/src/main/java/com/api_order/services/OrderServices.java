package com.api_order.services;

import com.api_order.config.InventoryClient;
import com.api_order.dto.inventory.ResponseProductDTO;
import com.api_order.dto.item.OrderItemRequest;
import com.api_order.dto.order.CreateOrderRequest;
import com.api_order.dto.order.ResponseOrderDTO;
import com.api_order.dto.order.StockDetail;
import com.api_order.exceptions.client.ServiceUnavailableException;
import com.api_order.exceptions.order.InsufficientStockException;
import com.api_order.exceptions.order.OrderAlreadyCancelledException;
import com.api_order.exceptions.order.OrderNotFoundException;
import com.api_order.model.order.OrderModel;
import com.api_order.model.order.OrderStatus;
import com.api_order.model.orderItem.OrderItemModel;
import com.api_order.repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

//TODO criar exceptions and exception handler

@Service
public class OrderServices implements IOrderServices {

    private final OrderRepository orderRepository;
    private final InventoryClient inventoryClient;

    public OrderServices(OrderRepository orderRepository, InventoryClient inventoryClient) {
        this.orderRepository = orderRepository;
        this.inventoryClient = inventoryClient;
    }

    @Override
    @Transactional
    public ResponseOrderDTO createOrder(CreateOrderRequest data) {
        if (!this.inventoryClient.verifyIfInventoryApiIsUp()) {
            throw new ServiceUnavailableException("Inventory service is unavailable");
        }

        BigDecimal total = BigDecimal.ZERO;
        List<StockDetail> stockProblems = new ArrayList<>();
        List<OrderItemModel> orderItems = new ArrayList<>();

        for (OrderItemRequest item : data.items()) {
            ResponseProductDTO product = this.inventoryClient.getProduct(item.productId());

            if (product.stock() < item.qty()) {
                throw new InsufficientStockException(item.productId(), item.qty(), product.stock());
            }

            this.inventoryClient.updateStock(item.productId(), -item.qty());

            BigDecimal itemTotal = product.price().multiply(BigDecimal.valueOf(item.qty()));
            total = total.add(itemTotal);

            OrderItemModel orderItem = new OrderItemModel();
            orderItem.setProduct_id(item.productId());
            orderItem.setQty(item.qty());
            orderItem.setUnitPrice(product.price());

            orderItems.add(orderItem);
        }
        if (!stockProblems.isEmpty()) {
            throw new InsufficientStockException("Insufficient stock for order");
        }

        // Cria o pedido sem os itens primeiro
        OrderModel order = new OrderModel();
        order.setStatus(OrderStatus.CREATED);
        order.setTotal(total);
        order.setCreatedAt(LocalDateTime.now());

        // Salva para gerar o ID
        OrderModel savedOrder = orderRepository.save(order);

        // Agora define o order_id nos itens
        for (OrderItemModel item : orderItems) {
            item.setOrder(savedOrder); // <-- aqui é crucial
        }

        savedOrder.setItems(orderItems);

        // Salva novamente com os itens associados
        savedOrder = orderRepository.save(savedOrder);

        return new ResponseOrderDTO(savedOrder);
    }

    @Override
    public ResponseOrderDTO getOrderForId(UUID orderId) {
        var order = orderRepository.findById(orderId)
                .orElseThrow(OrderNotFoundException::new);

        return new ResponseOrderDTO(order);
    }

    @Override
    public Page<ResponseOrderDTO> getOrders(Pageable pageable) {
        Page<OrderModel> order = orderRepository.findAll(pageable);

        if (!order.hasContent()) {
            throw new OrderNotFoundException("Orders empty");
        }

        return order.map(ResponseOrderDTO::new);
    }

    @Override
    @Transactional
    public void cancelOrder(UUID orderId) {
        OrderModel order = orderRepository.findById(orderId)
                .orElseThrow(OrderNotFoundException::new);

        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new OrderAlreadyCancelledException(order.getId());
        }

        // Restaura o estoque
        for (OrderItemModel item : order.getItems()) {
            inventoryClient.updateStock(item.getProduct_id(), item.getQty());
        }

        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
    }
}
