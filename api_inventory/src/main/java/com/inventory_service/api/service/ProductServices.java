package com.inventory_service.api.service;

import com.inventory_service.api.dto.CreateProductDTO;
import com.inventory_service.api.dto.ResponseProductDTO;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServices implements ProductServicesInterface{



    @Override
    @Transactional
    public ResponseProductDTO createProduct(CreateProductDTO data) {
        return null;
    }

    @Override
    public List<ResponseProductDTO> getAllProducts() {
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
