package com.api_order.model.orderItem;

import com.api_order.model.order.OrderModel;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Table(name = "order_items")
@Entity(name = "item")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class OrderItemModel {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private OrderModel order_id;

    private UUID productId;

    private Integer qty;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal unitPrice;
}
