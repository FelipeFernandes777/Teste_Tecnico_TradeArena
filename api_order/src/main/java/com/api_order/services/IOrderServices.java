package com.api_order.services;

import com.api_order.dto.order.CreateOrderRequest;
import com.api_order.dto.order.ResponseOrderDTO;
import org.aspectj.apache.bcel.classfile.Unknown;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface IOrderServices {
    ResponseOrderDTO createOrder(CreateOrderRequest data);
    ResponseOrderDTO getOrderForId(UUID orderId);
    Page<ResponseOrderDTO> getOrders(Pageable pageable);
    void cancelOrder(UUID orderId);
}
