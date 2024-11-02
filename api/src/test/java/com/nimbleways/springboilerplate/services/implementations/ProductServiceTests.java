package com.nimbleways.springboilerplate.services.implementations;

import com.nimbleways.springboilerplate.entities.Product;
import com.nimbleways.springboilerplate.exceptions.ProductNotFoundException;
import com.nimbleways.springboilerplate.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceTests {


    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void createProduct_ShouldReturnCreatedProduct() {
        // Arrange
        Product product = new Product();
        product.setName("New Product");
        when(productRepository.save(product)).thenReturn(product);

        // Act
        Product createdProduct = productService.createProduct(product);

        // Assert
        assertNotNull(createdProduct);
        assertEquals("New Product", createdProduct.getName());
        verify(productRepository, times(1)).save(product);
    }

    @Test
    void updateProduct_ShouldReturnUpdatedProduct_WhenProductExists() throws ProductNotFoundException {
        // Arrange
        Long productId = 1L;
        Product existingProduct = new Product();
        existingProduct.setId(productId);
        existingProduct.setName("Old Product");

        Product updatedProduct = new Product();
        updatedProduct.setName("Updated Product");

        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(existingProduct)).thenReturn(existingProduct);

        // Act
        Product result = productService.updateProduct(productId, updatedProduct);

        // Assert
        assertEquals("Updated Product", result.getName());
        verify(productRepository, times(1)).findById(productId);
        verify(productRepository, times(1)).save(existingProduct);
    }

    @Test
    void updateProduct_ShouldThrowProductNotFoundException_WhenProductDoesNotExist() {
        // Arrange
        Long productId = 1L;
        Product updatedProduct = new Product();
        updatedProduct.setName("Updated Product");

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        // Act & Assert
        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class, () ->
                productService.updateProduct(productId, updatedProduct)
        );

        assertEquals("Product not found", exception.getMessage());
        verify(productRepository, times(1)).findById(productId);
        verify(productRepository, never()).save(any(Product.class));
    }


}