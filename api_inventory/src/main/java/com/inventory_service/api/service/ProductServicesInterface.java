package com.inventory_service.api.service;

import com.inventory_service.api.dto.CreateProductDTO;
import com.inventory_service.api.dto.ResponseProductDTO;

import java.util.List;
import java.util.Optional;

public interface ProductServicesInterface {
    ResponseProductDTO createProduct(CreateProductDTO data);
    List<ResponseProductDTO> getAllProducts();
    Optional<ResponseProductDTO> getProductForId(String productId);
    void health();
}
