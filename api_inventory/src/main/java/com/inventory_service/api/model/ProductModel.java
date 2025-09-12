package com.inventory_service.api.model;

import com.inventory_service.api.dto.CreateProductDTO;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CurrentTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Table(name = "products")
@Entity(name = "product")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class ProductModel {
    @Id
    @GeneratedValue
    private UUID id;
    private String sku;
    private String name;
    @Column(precision = 10, scale = 2)
    private BigDecimal price;
    private int stock;
    @CurrentTimestamp
    private LocalDateTime createdAt;

    public ProductModel(CreateProductDTO data) {
        this.name = data.name();
        this.price = data.price();
        this.stock = data.stock();
        this.sku = data.sku();
    }

    public void changeStock(int newStock) {
        this.stock = newStock;
    }
}
