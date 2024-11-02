package com.nimbleways.springboilerplate;

import com.nimbleways.springboilerplate.controllers.ProductsController;
import com.nimbleways.springboilerplate.dto.product.ProductDTO;
import com.nimbleways.springboilerplate.entities.Product;
import com.nimbleways.springboilerplate.enums.ProductType;
import com.nimbleways.springboilerplate.exceptions.ProductNotFoundException;
import com.nimbleways.springboilerplate.repositories.ProductRepository;
import com.nimbleways.springboilerplate.services.implementations.ProductService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class ApplicationIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductsController productsController;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        // Ensures a clean database state before each test
        productRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
        // Clean up the database after each test
        productRepository.deleteAll();
    }

    @Test
    void contextLoads() {
        // Verifies the main components are loaded into the application context
        assertThat(productsController).isNotNull();
        assertThat(productService).isNotNull();
        assertThat(productRepository).isNotNull();

    }

    @Test
    void productsController_CanCreateProduct() {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName("Integration Test Product");
        productDTO.setAvailable(10);
        productDTO.setType(ProductType.NORMAL);

        ResponseEntity<ProductDTO> response = productsController.createProduct(productDTO);
        assertThat(response.getStatusCodeValue()).isEqualTo(201);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Integration Test Product");

        // Clean up
        productRepository.deleteById(response.getBody().getId());
    }


    @Test
    void productService_CanCreateAndUpdateProduct() throws ProductNotFoundException {
        // Arrange - create a product using the service
        Product product = new Product();
        product.setName("Service Test Product");
        product.setAvailable(5);
        product.setType(ProductType.EXPIRABLE);

        Product createdProduct = productService.createProduct(product);
        assertThat(createdProduct).isNotNull();
        assertThat(createdProduct.getId()).isNotNull();
        assertEquals("Service Test Product", createdProduct.getName());

        // Act - update the product's details
        createdProduct.setName("Updated Service Product");
        createdProduct.setAvailable(20);
        Product updatedProduct = productService.updateProduct(createdProduct.getId(), createdProduct);

        // Assert - verify the updated details
        Optional<Product> retrievedProduct = productRepository.findById(updatedProduct.getId());
        assertThat(retrievedProduct).isPresent();
        assertEquals("Updated Service Product", retrievedProduct.get().getName());
        assertEquals(20, retrievedProduct.get().getAvailable());
    }

    @Test
    void productController_ShouldReturnNotFound_WhenUpdatingNonExistentProduct() throws Exception {
        // Attempt to update a product that doesn't exist
        Product updatedProduct = new Product();
        updatedProduct.setName("Non-Existent Product");
        updatedProduct.setAvailable(0);
        updatedProduct.setType(ProductType.NORMAL);

        mockMvc.perform(put("/products/{productId}", 999L)
                        .contentType("application/json")
                        .content("{\"name\":\"Non-Existent Product\",\"available\":0}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void productRepository_CanPerformCrudOperations() {
        // Create
        Product product = new Product();
        product.setName("Repository Test Product");
        product.setAvailable(15);
        product.setType(ProductType.SEASONAL);

        Product savedProduct = productRepository.save(product);
        assertThat(savedProduct).isNotNull();
        assertThat(savedProduct.getId()).isNotNull();

        // Read
        Optional<Product> foundProduct = productRepository.findById(savedProduct.getId());
        assertThat(foundProduct).isPresent();
        assertEquals("Repository Test Product", foundProduct.get().getName());

        // Update
        savedProduct.setAvailable(20);
        productRepository.save(savedProduct);

        Optional<Product> updatedProduct = productRepository.findById(savedProduct.getId());
        assertThat(updatedProduct).isPresent();
        assertEquals(20, updatedProduct.get().getAvailable());

        // Delete
        productRepository.deleteById(savedProduct.getId());
        assertThat(productRepository.findById(savedProduct.getId())).isEmpty();


    }
}
