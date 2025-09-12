package com.api_order.model.order;

import com.api_order.model.orderItem.OrderItemModel;
import jakarta.persistence.*;
import lombok.*;
import org.aspectj.apache.bcel.classfile.Unknown;
import org.hibernate.annotations.CurrentTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Table(name = "orders")
@Entity(name = "order")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class OrderModel {
    @Id
    @GeneratedValue()
    private UUID id;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal total;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItemModel> items = new ArrayList<>();

    @CurrentTimestamp
    private LocalDateTime createdAt;
}
