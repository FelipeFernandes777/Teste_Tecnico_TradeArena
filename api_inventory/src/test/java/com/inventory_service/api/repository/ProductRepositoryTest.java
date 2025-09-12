package com.inventory_service.api.repository;

import com.inventory_service.api.model.ProductModel;
import com.inventory_service.api.dto.CreateProductDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    @DisplayName("Should save and find a product by ID")
    void saveAndFindById() {
        CreateProductDTO dto = new CreateProductDTO("SKU123", "Produto Teste", new BigDecimal("10.00"), 5);
        ProductModel product = new ProductModel(dto);

        ProductModel saved = productRepository.save(product);

        assertNotNull(saved.getId(), "ID should be generated");
        Optional<ProductModel> found = productRepository.findById(saved.getId());
        assertTrue(found.isPresent(), "Product should be found by ID");
        assertEquals("Produto Teste", found.get().getName());
    }
}
