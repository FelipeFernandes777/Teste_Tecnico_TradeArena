package com.inventory_service.api.controller;

import com.inventory_service.api.dto.CreateProductDTO;
import com.inventory_service.api.dto.ResponseProductDTO;
import com.inventory_service.api.service.ProductServices;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Map;

//TODO finalizar api com o metodo path


@RestController
@RequestMapping("/products")
public class ProductController {
    private final DefaultTransactionDefinition defaultTransactionDefinition;
    private ProductServices productServices;

    public ProductController(ProductServices productServices, DefaultTransactionDefinition defaultTransactionDefinition) {
        this.productServices = productServices;
        this.defaultTransactionDefinition = defaultTransactionDefinition;
    }

    @PostMapping()
    public ResponseEntity<ResponseProductDTO> create(@RequestBody CreateProductDTO data) {
        var newProduct = productServices.createProduct(data);

        var location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newProduct.id())
                .toUri();

        return ResponseEntity.status(HttpStatus.CREATED).location(location).body(newProduct);
    }

    @GetMapping()
    public ResponseEntity<Page<ResponseProductDTO>> getProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        Pageable pageable = PageRequest.of(page, size);
        var  products = this.productServices.getAllProducts(pageable);

        return ResponseEntity.ok().body(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseProductDTO> getProductForId(@PathVariable String id) {
        var  products = this.productServices.getProductForId(id);
        return ResponseEntity.ok().body(products);
    }

    @GetMapping("/health")
    public ResponseEntity<?> getHealth() {
        return ResponseEntity.ok().body(
                Map.of(
                        "status", true,
                        "message", "Product service is up!"
                )
        );
    }

    @PatchMapping("/{id}/stock")
    public ResponseEntity<ResponseProductDTO> adjustStock(@PathVariable String id, @RequestParam int quantity) {
        ResponseProductDTO updatedStock = productServices.adjustStock(id, quantity);
        return ResponseEntity.ok().body(updatedStock);
    }
}
