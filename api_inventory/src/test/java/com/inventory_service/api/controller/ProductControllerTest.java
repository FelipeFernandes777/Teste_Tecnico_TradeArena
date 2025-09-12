package com.inventory_service.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inventory_service.api.dto.CreateProductDTO;
import com.inventory_service.api.dto.ResponseProductDTO;
import com.inventory_service.api.service.ProductServices;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductServices productServices;

    @Test
    @DisplayName("POST /products - Success")
    void createProductSuccess() throws Exception {
        CreateProductDTO dto = new CreateProductDTO("ABC123", "Produto Teste", new BigDecimal("10.00"), 5);
        ResponseProductDTO responseDTO = new ResponseProductDTO(
                UUID.randomUUID(),
                "Produto Teste",
                "ABC123",
                new BigDecimal("10.00"),
                5,
                LocalDateTime.now()
        );

        when(productServices.createProduct(any(CreateProductDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.name").value("Produto Teste"))
                .andExpect(jsonPath("$.sku").value("ABC123"))
                .andExpect(jsonPath("$.price").value(10.00))
                .andExpect(jsonPath("$.stock").value(5));
    }

    @Test
    @DisplayName("POST /products - Failure (missing name)")
    void createProductFailure() throws Exception {
        CreateProductDTO dto = new CreateProductDTO("ABC123", "", new BigDecimal("10.00"), 5);

        when(productServices.createProduct(any(CreateProductDTO.class)))
                .thenThrow(new RuntimeException("Product data is missing"));

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("GET /products - Success")
    void getProductsSuccess() throws Exception {
        ResponseProductDTO product1 = new ResponseProductDTO(
                UUID.randomUUID(), "Produto 1", "SKU1", new BigDecimal("10.00"), 5, LocalDateTime.now());
        ResponseProductDTO product2 = new ResponseProductDTO(
                UUID.randomUUID(), "Produto 2", "SKU2", new BigDecimal("20.00"), 3, LocalDateTime.now());

        when(productServices.getAllProducts(any(PageRequest.class)))
                .thenReturn(new PageImpl<>(List.of(product1, product2)));

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Produto 1"))
                .andExpect(jsonPath("$.content[1].name").value("Produto 2"));
    }

    @Test
    @DisplayName("GET /products/{id} - Success")
    void getProductByIdSuccess() throws Exception {
        UUID id = UUID.randomUUID();
        ResponseProductDTO responseDTO = new ResponseProductDTO(
                id, "Produto Teste", "ABC123", new BigDecimal("10.00"), 5, LocalDateTime.now());

        when(productServices.getProductForId(eq(id))).thenReturn(responseDTO);

        mockMvc.perform(get("/products/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Produto Teste"))
                .andExpect(jsonPath("$.sku").value("ABC123"));
    }

    @Test
    @DisplayName("GET /products/{id} - Failure (not found)")
    void getProductByIdFailure() throws Exception {
        UUID id = UUID.randomUUID();

        when(productServices.getProductForId(eq(id)))
                .thenThrow(new RuntimeException("Product not found"));

        mockMvc.perform(get("/products/{id}", id))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("An unexpected error occurred: Product not found"));
    }

    @Test
    @DisplayName("PATCH /products/{id}/stock - Success (increase)")
    void adjustStockSuccess() throws Exception {
        UUID id = UUID.randomUUID();
        ResponseProductDTO responseDTO = new ResponseProductDTO(
                id, "Produto Teste", "ABC123", new BigDecimal("10.00"), 7, LocalDateTime.now());

        when(productServices.adjustStock(eq(id), eq(2))).thenReturn(responseDTO);

        mockMvc.perform(patch("/products/{id}/stock", id)
                        .param("quantity", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stock").value(7));
    }

    @Test
    @DisplayName("PATCH /products/{id}/stock - Failure (not enough stock)")
    void adjustStockFailure() throws Exception {
        UUID id = UUID.randomUUID();

        when(productServices.adjustStock(eq(id), eq(-10)))
                .thenThrow(new RuntimeException("Not enough stock to remove 10"));

        mockMvc.perform(patch("/products/{id}/stock", id)
                        .param("quantity", "-10"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("An unexpected error occurred: Not enough stock to remove 10"));
    }

    @Test
    @DisplayName("GET /products/health - Success")
    void healthCheck() throws Exception {
        mockMvc.perform(get("/products/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.message").value("Product service is up!"));
    }
}
