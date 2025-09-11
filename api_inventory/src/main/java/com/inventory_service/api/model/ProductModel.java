package com.inventory_service.api.model;

import com.inventory_service.api.dto.CreateProductDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CurrentTimestamp;

import java.time.LocalDateTime;

@Table(name = "products")
@Entity(name = "product")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@EqualsAndHashCode(of = "id")
public class ProductModel {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String sku;
    private String name;
    private Float price;
    private int stock;
    @CurrentTimestamp
    private LocalDateTime createdAt;

    public ProductModel(CreateProductDTO data) {
        this.name = data.name();
        this.price = data.price();
        this.stock = data.stock();
        this.sku = data.sku();
    }
}
