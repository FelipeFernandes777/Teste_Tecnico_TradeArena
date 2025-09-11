package com.inventory_service.api.service;

import com.inventory_service.api.dto.CreateProductDTO;
import com.inventory_service.api.dto.ResponseProductDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ProductServicesInterface {
    ResponseProductDTO createProduct(CreateProductDTO data);
    Page<ResponseProductDTO> getAllProducts(Pageable pageable);
    ResponseProductDTO getProductForId(UUID productId);
    ResponseProductDTO adjustStock(UUID productId, int quantity);
}
