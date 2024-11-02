package com.nimbleways.springboilerplate.controllers;

import com.nimbleways.springboilerplate.dto.product.ProductDTO;
import com.nimbleways.springboilerplate.entities.Product;
import com.nimbleways.springboilerplate.mappers.ProductMapper;
import com.nimbleways.springboilerplate.services.implementations.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductsController.class)
class ProductsControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @MockBean
    private ProductMapper productMapper;

    @InjectMocks
    private ProductsController productsController;

    @BeforeEach
    void setUp() {

        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(productsController).build();
    }


    @Test
    void createProduct_ShouldReturnCreatedProduct() throws Exception {
        // Arrange
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(1L);  // Set the ID in the ProductDTO
        productDTO.setName("New Product");

        Product product = new Product();
        product.setId(1L);
        product.setName("New Product");

        // Mock mapper and service behavior
        when(productMapper.toEntity(any(ProductDTO.class))).thenReturn(product);
        when(productService.createProduct(any(Product.class))).thenReturn(product);
        when(productMapper.toDTO(any(Product.class))).thenReturn(productDTO);

        // Act & Assert
        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"New Product\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("New Product"));

        verify(productService, times(1)).createProduct(any(Product.class));

    }

    @Test
    void updateProduct_ShouldReturnUpdatedProduct_WhenProductExists() throws Exception {
        // Arrange
        Long productId = 1L;
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName("Updated Product");
        productDTO.setAvailable(10);
        productDTO.setLeadTime(5);

        Product product = new Product();
        product.setId(productId);
        product.setName("Updated Product");
        product.setAvailable(10);
        product.setLeadTime(5);

        // Mock mapper behavior to convert ProductDTO to Product and vice versa
        when(productMapper.toEntity(any(ProductDTO.class))).thenReturn(product);
        when(productMapper.toDTO(any(Product.class))).thenReturn(productDTO);

        // Mock service behavior to return the updated Product
        when(productService.updateProduct(eq(productId), any(Product.class))).thenReturn(product);

        // Act & Assert
        mockMvc.perform(put("/products/{productId}", productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Updated Product\",\"available\":10,\"leadTime\":5}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Product"))
                .andExpect(jsonPath("$.available").value(10))
                .andExpect(jsonPath("$.leadTime").value(5));

        // Verify that the service and mapper methods were called correctly
        verify(productMapper, times(1)).toEntity(any(ProductDTO.class));
        verify(productMapper, times(1)).toDTO(any(Product.class));
        verify(productService, times(1)).updateProduct(eq(productId), any(Product.class));
    }




}