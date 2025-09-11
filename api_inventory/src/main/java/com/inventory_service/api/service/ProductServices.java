package com.inventory_service.api.service;

import com.inventory_service.api.dto.CreateProductDTO;
import com.inventory_service.api.dto.ResponseProductDTO;
import com.inventory_service.api.model.ProductModel;
import com.inventory_service.api.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

//TODO criar middleware de exception

@Service
public class ProductServices implements ProductServicesInterface{
    private ProductRepository repository;

    public ProductServices(ProductRepository productRepository) {
        this.repository = productRepository;
    }

    @Override
    @Transactional
    public ResponseProductDTO createProduct(CreateProductDTO data) {
        try {
            if(data.name().isEmpty() || data.sku().isEmpty() || data.price() == 0 || data.stock() == 0) {
                throw new Exception("All fields are required");
            }

            var newProduct = new ProductModel(data);
            this.repository.save(newProduct);

            return new ResponseProductDTO(newProduct);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Page<ResponseProductDTO> getAllProducts(Pageable pageable) {
        return List.of();
    }

    @Override
    public Optional<ResponseProductDTO> getProductForId(String productId) {
        return Optional.empty();
    }

    @Override
    public void health() {

    }
}
