package com.inventory_service.api.repository;

import com.inventory_service.api.model.ProductModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<ProductModel, String> {}
