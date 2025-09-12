package com.inventory_service.api.service;

import com.inventory_service.api.dto.CreateProductDTO;
import com.inventory_service.api.dto.ResponseProductDTO;
import com.inventory_service.api.model.ProductModel;
import com.inventory_service.api.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServicesTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServices productServices;

    @Test
    @DisplayName("Should return a new product with success")
    void createProductCase1() {
        CreateProductDTO dto = new CreateProductDTO("abc123", "Produto_Teste", new BigDecimal("10.00"), 1);
        ProductModel savedProduct = new ProductModel(dto);

        when(productRepository.save(any(ProductModel.class))).thenReturn(savedProduct);

        ResponseProductDTO response = productServices.createProduct(dto);

        assertNotNull(response);
        assertEquals("Produto_Teste", response.name());
        verify(productRepository, times(1)).save(any(ProductModel.class));
    }

    @Test
    @DisplayName("Should return empty when product not found by id")
    void getProductForIdNotFound() {
        UUID id = UUID.randomUUID();
        when(productRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> productServices.getProductForId(id));
        verify(productRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("Should return a product when id is informed")
    void getProductForId() {
        UUID id = UUID.randomUUID();
        CreateProductDTO dto = new CreateProductDTO("abc123", "Produto_Teste", new BigDecimal("10.00"), 1);
        ProductModel savedProduct = new ProductModel(dto);
        savedProduct.setId(id);

        when(productRepository.findById(id)).thenReturn(Optional.of(savedProduct));

        ResponseProductDTO response = productServices.getProductForId(id);

        assertNotNull(response);
        assertEquals("Produto_Teste", response.name());
        verify(productRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("Should adjust stock successfully")
    void adjustStock() {
        UUID id = UUID.randomUUID();
        ProductModel product = new ProductModel(new CreateProductDTO("abc123", "Produto_Teste", new BigDecimal("15.00"), 10));
        product.setId(id);

        when(productRepository.findById(id)).thenReturn(Optional.of(product));
        when(productRepository.save(any(ProductModel.class))).thenReturn(product);

        ResponseProductDTO response = productServices.adjustStock(id, -5);

        assertNotNull(response);
        assertEquals(5, response.stock()); // 10 - 5
        verify(productRepository, times(1)).save(any(ProductModel.class));
    }
}
