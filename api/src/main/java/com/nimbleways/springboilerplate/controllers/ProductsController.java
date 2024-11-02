package com.nimbleways.springboilerplate.controllers;

import com.nimbleways.springboilerplate.dto.product.ProductDTO;
import com.nimbleways.springboilerplate.entities.Product;
import com.nimbleways.springboilerplate.exceptions.ProductNotFoundException;
import com.nimbleways.springboilerplate.mappers.ProductMapper;
import com.nimbleways.springboilerplate.services.implementations.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


/**
 * REST controller for managing products.
 * Provides endpoints for creating and updating product entities.
 */
@RestController
@RequestMapping("/products")
public class ProductsController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductsController.class);

    private ProductService productService;
    private ProductMapper productMapper;

    /**
     * Constructor to initialize the ProductsController with the specified ProductService.
     *
     * @param productService the service layer to handle product operations
     * @param productMapper  the mapper to convert between Product entities and ProductDTOs
     */
    public ProductsController(ProductService productService, ProductMapper productMapper) {
        this.productService = productService;
        this.productMapper = productMapper;
    }


    /**
     * Creates a new product based on the provided ProductDTO.
     *
     * @param productDTO the product data transfer object containing product details
     * @return ResponseEntity containing the created ProductDTO and HTTP status 201 (Created)
     */
    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTO productDTO) {
        LOGGER.info("Received request to create product with name: {}", productDTO.getName());
        Product product = productMapper.toEntity(productDTO);  // Map DTO to entity
        Product createdProduct = productService.createProduct(product);
        ProductDTO createdProductDTO = productMapper.toDTO(createdProduct);  // Map entity back to DTO
        LOGGER.info("Product created successfully with ID: {}", createdProduct.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProductDTO);
    }

    /**
     * Updates an existing product by its ID.
     *
     * @param productId  the ID of the product to update
     * @param productDTO the product details in DTO format for the update
     * @return ResponseEntity containing the updated product as a ProductDTO and HTTP status 200 (OK),
     *         or 404 (Not Found) if the product does not exist
     */
    @PutMapping("/{productId}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long productId, @RequestBody ProductDTO productDTO) throws ProductNotFoundException {
        LOGGER.info("Received request to update product with ID: {}", productId);

        // Map the DTO to the entity
        Product product = productMapper.toEntity(productDTO);

        try {
            Product updatedProduct = productService.updateProduct(productId, product);
            LOGGER.info("Product with ID: {} updated successfully", productId);
            return ResponseEntity.ok(productMapper.toDTO(updatedProduct));
        } catch (ProductNotFoundException e) {
            LOGGER.error("Product with ID {} not found: {}", productId, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

    }

}
