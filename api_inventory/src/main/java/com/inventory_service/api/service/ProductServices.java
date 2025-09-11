package com.inventory_service.api.service;

import com.inventory_service.api.dto.CreateProductDTO;
import com.inventory_service.api.dto.ResponseProductDTO;
import com.inventory_service.api.exceptions.DataValueIsMissingException;
import com.inventory_service.api.exceptions.FailedToCreateANewProductException;
import com.inventory_service.api.exceptions.NotFoundProductByIdException;
import com.inventory_service.api.exceptions.NotFoundProductsException;
import com.inventory_service.api.model.ProductModel;
import com.inventory_service.api.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;;

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
                throw new DataValueIsMissingException();
            }

            var newProduct = new ProductModel(data);
            this.repository.save(newProduct);

            return new ResponseProductDTO(newProduct);
        } catch (FailedToCreateANewProductException e) {
            throw new FailedToCreateANewProductException(e.getMessage());
        }
    }

    @Override
    public Page<ResponseProductDTO> getAllProducts(Pageable pageable) {
        try {
            Page<ProductModel> products = this.repository.findAll(pageable);

            if(!products.hasContent()) {
                throw new NotFoundProductsException("Product list is empty");
            }

            return products.map(ResponseProductDTO::new);

        } catch (NotFoundProductsException e) {
            throw new NotFoundProductsException(e.getMessage());
        }
    }

    @Override
    public ResponseProductDTO getProductForId(String productId) {
        try {
            if(productId.isEmpty()) {
                throw new DataValueIsMissingException("Product ID is empty");
            }
            ProductModel product = this.repository.findById(productId).orElseThrow(NotFoundProductByIdException::new);

            return new ResponseProductDTO(product);
        } catch (NotFoundProductByIdException e) {
            throw new NotFoundProductByIdException(e.getMessage());
        }
    }
}
