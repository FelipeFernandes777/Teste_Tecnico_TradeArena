package com.api_order.repository;

import com.api_order.model.orderItem.OrderItemModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderItemRepository extends JpaRepository<OrderItemModel, UUID> {}
