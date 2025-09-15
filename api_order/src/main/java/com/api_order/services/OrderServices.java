package com.api_order.services;

import com.api_order.config.InventoryClient;
import com.api_order.dto.ResponseOrderDTO;
import com.api_order.model.order.OrderModel;
import com.api_order.repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.aspectj.apache.bcel.classfile.Unknown;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
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
    public ResponseOrderDTO createOrder(Unknown data) {
        return null;
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
