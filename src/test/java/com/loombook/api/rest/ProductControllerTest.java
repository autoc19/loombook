package com.loombook.api.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * ProductController API 測試
 */
@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("POST /api/products - 201 建立成功")
    void shouldReturn201WhenCreateProductSuccessfully() throws Exception {
        CreateProductRequest request = new CreateProductRequest("Java 程式設計", 9900);

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.name").value("Java 程式設計"))
                .andExpect(jsonPath("$.priceCents").value(9900));
    }

    @Test
    @DisplayName("POST /api/products - 400 name 空白")
    void shouldReturn400WhenNameIsBlank() throws Exception {
        CreateProductRequest request = new CreateProductRequest("", 9900);

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/products - 400 priceCents 為負數")
    void shouldReturn400WhenPriceIsNegative() throws Exception {
        CreateProductRequest request = new CreateProductRequest("Test", -1);

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /api/products/{id} - 200 商品存在")
    void shouldReturn200WhenProductExists() throws Exception {
        // 先建立商品
        CreateProductRequest request = new CreateProductRequest("Spring Boot 實戰", 12000);
        MvcResult createResult = mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();

        ProductResponse created = objectMapper.readValue(
                createResult.getResponse().getContentAsString(), ProductResponse.class);

        // 查詢商品
        mockMvc.perform(get("/api/products/{id}", created.id()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(created.id()))
                .andExpect(jsonPath("$.name").value("Spring Boot 實戰"))
                .andExpect(jsonPath("$.priceCents").value(12000));
    }

    @Test
    @DisplayName("GET /api/products/{id} - 404 商品不存在")
    void shouldReturn404WhenProductNotExists() throws Exception {
        mockMvc.perform(get("/api/products/{id}", "not-exist-id"))
                .andExpect(status().isNotFound());
    }
}
