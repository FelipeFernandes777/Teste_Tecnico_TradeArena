package com.inventory_service.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inventory_service.api.config.SecurityConfigTest;
import com.inventory_service.api.dto.CreateProductDTO;
import com.inventory_service.api.dto.ResponseProductDTO;
import com.inventory_service.api.service.ProductServices;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
@Import(SecurityConfigTest.class)
@AutoConfigureMockMvc(addFilters = false) // DESABILITA filtros de segurança
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductServices productServices;

    @MockBean
    private com.inventory_service.api.config.security.ApiKeyValidator apiKeyValidator;

    @Autowired
    private ObjectMapper objectMapper;

    private ResponseProductDTO mockProduct;

    @BeforeEach
    void setup() {
        mockProduct = new ResponseProductDTO(
                UUID.randomUUID(),
                "Notebook Gamer",
                "Um notebook top para jogos",
                BigDecimal.valueOf(7500.00),
                10,
                LocalDateTime.now()
        );
    }

    @Test
    @DisplayName("Create a new product")
    void testCreateProduct() throws Exception {
        CreateProductDTO dto = new CreateProductDTO("Notebook Gamer", "Um notebook top", BigDecimal.valueOf(7500.00), 10);

        when(productServices.createProduct(any(CreateProductDTO.class))).thenReturn(mockProduct);

        String response = mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        ResponseProductDTO responseDTO = objectMapper.readValue(response, ResponseProductDTO.class);

        assertThat(responseDTO.id()).isEqualTo(mockProduct.id());
        assertThat(responseDTO.name()).isEqualTo("Notebook Gamer");
        assertThat(responseDTO.sku()).isEqualTo("Um notebook top para jogos");
        assertThat(responseDTO.price()).isEqualByComparingTo(BigDecimal.valueOf(7500.00));
        assertThat(responseDTO.stock()).isEqualTo(10);
    }

    @Test
    @DisplayName("Get product by ID")
    void testGetProductById() throws Exception {
        when(productServices.getProductForId(mockProduct.id())).thenReturn(mockProduct);

        String response = mockMvc.perform(get("/products/" + mockProduct.id()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        ResponseProductDTO responseDTO = objectMapper.readValue(response, ResponseProductDTO.class);

        assertThat(responseDTO.id()).isEqualTo(mockProduct.id());
        assertThat(responseDTO.name()).isEqualTo("Notebook Gamer");
        assertThat(responseDTO.sku()).isEqualTo("Um notebook top para jogos");
    }

    @Test
    @DisplayName("Verify if apis is up")
    void testHealthEndpoint() throws Exception {
        String response = mockMvc.perform(get("/products/health"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // Desserializa como Map para endpoints simples
        var map = objectMapper.readValue(response, java.util.Map.class);

        assertThat(map.get("status")).isEqualTo(true);
        assertThat(map.get("message")).isEqualTo("Product service is up!");
    }
}
