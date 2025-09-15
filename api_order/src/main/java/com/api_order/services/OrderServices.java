package com.api_order.services;

import com.api_order.config.InventoryClient;
import com.api_order.dto.inventory.ResponseProductDTO;
import com.api_order.dto.item.OrderItemRequest;
import com.api_order.dto.order.CreateOrderRequest;
import com.api_order.dto.order.ResponseOrderDTO;
import com.api_order.dto.order.StockDetail;
import com.api_order.model.order.OrderModel;
import com.api_order.model.order.OrderStatus;
import com.api_order.model.orderItem.OrderItemModel;
import com.api_order.repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

//TODO criar exceptions and exception handler

public class OrderServices implements IOrderServices{

    private final OrderRepository orderRepository;
    private final InventoryClient inventoryClient;

    public OrderServices(OrderRepository orderRepository,InventoryClient inventoryClient) {
        this.orderRepository = orderRepository;
        this.inventoryClient = inventoryClient;
    }

    @Override
    @Transactional
    public ResponseOrderDTO createOrder(CreateOrderRequest data) {
        if(!this.inventoryClient.verifyIfInventoryApiIsUp()){
            throw new RuntimeException("Inventory service is unavailable");
        }

        BigDecimal total = BigDecimal.ZERO;
        List<StockDetail> stockProblems =  new ArrayList<>(); // SotckDetail
        List<OrderItemModel> orderItems =  new ArrayList<>();

        for(OrderItemRequest item: data.items()) {
            try {
                ResponseProductDTO product = this.inventoryClient.getProduct(item.productId()); // Percore a lista e pega o item com o id informado

                if (product.stock() < item.qty()) {
                    stockProblems.add(new StockDetail(
                            item.productId(),
                            item.qty(),
                            product.stock()
                    ));
                }

                this.inventoryClient.updateStock(item.productId(), -item.qty()); // Já pega o stock do produto e subtrai pela quantidade informada

                BigDecimal itemTotal = product.price().multiply(BigDecimal.valueOf(item.qty())); // Pega o valor do item e já multiplica pela quantidade
                total = total.add(itemTotal);

                OrderItemModel orderItem = new OrderItemModel();
                orderItem.setProduct_id(item.productId());
                orderItem.setQty(item.qty());
                orderItem.setUnitPrice(product.price());

                orderItems.add(orderItem);

            } catch (RuntimeException e) {
                throw new RuntimeException("Error processing product: " + item.productId(), e);
            }
        }
        if (!stockProblems.isEmpty()) {
            throw new RuntimeException("Insufficient stock");
        }

        OrderModel order = new OrderModel();
        order.setStatus(OrderStatus.CREATED);
        order.setTotal(total);
        order.setItems(orderItems);
        order.setCreatedAt(LocalDateTime.now());

        OrderModel savedOrder = orderRepository.save(order);
        return new ResponseOrderDTO(savedOrder);
    }

    @Override
    public ResponseOrderDTO getOrderForId(UUID orderId) {
        try {
            var order =  orderRepository.findById(orderId)
                    .orElseThrow(RuntimeException::new);

            return new ResponseOrderDTO(order);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Page<ResponseOrderDTO> getOrders(Pageable pageable) {
        try {
            Page<OrderModel> order =  orderRepository.findAll(pageable);

            if(!order.hasContent()){
                throw new RuntimeException("Orders empty");
            }

            return order.map(ResponseOrderDTO::new);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional
    public void cancelOrder(UUID orderId) {
        try {
            var order =  orderRepository.findById(orderId)
                    .orElseThrow(RuntimeException::new);



        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
