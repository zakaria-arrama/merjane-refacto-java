package com.nimbleways.springboilerplate.mappers;

import com.nimbleways.springboilerplate.dto.product.ProductDTO;
import com.nimbleways.springboilerplate.entities.Product;
import com.nimbleways.springboilerplate.enums.ProductType;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

class ProductMapperTests {

    private final ProductMapper productMapper = Mappers.getMapper(ProductMapper.class);

    @Test
    void shouldMapProductToProductDTO() {
        // Arrange
        Product product = new Product();
        product.setName("Test Product");
        product.setAvailable(10);
        product.setLeadTime(5);
        product.setType(ProductType.NORMAL);
        product.setExpiryDate(LocalDate.now().plusDays(30));
        product.setSeasonStartDate(LocalDate.now().minusDays(10));
        product.setSeasonEndDate(LocalDate.now().plusDays(50));

        // Act
        ProductDTO productDTO = productMapper.toDTO(product);

        // Assert
        assertNotNull(productDTO);
        assertEquals(product.getName(), productDTO.getName());
        assertEquals(product.getAvailable(), productDTO.getAvailable());
        assertEquals(product.getLeadTime(), productDTO.getLeadTime());
        assertEquals(product.getType(), productDTO.getType());
        assertEquals(product.getExpiryDate(), productDTO.getExpiryDate());
        assertEquals(product.getSeasonStartDate(), productDTO.getSeasonStartDate());
        assertEquals(product.getSeasonEndDate(), productDTO.getSeasonEndDate());
    }

    @Test
    void shouldMapProductDTOToProduct() {
        // Arrange
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName("Test Product DTO");
        productDTO.setAvailable(20);
        productDTO.setLeadTime(7);
        productDTO.setType(ProductType.SEASONAL);
        productDTO.setExpiryDate(LocalDate.now().plusDays(40));
        productDTO.setSeasonStartDate(LocalDate.now().minusDays(20));
        productDTO.setSeasonEndDate(LocalDate.now().plusDays(60));

        // Act
        Product product = productMapper.toEntity(productDTO);

        // Assert
        assertNotNull(product);
        assertEquals(productDTO.getName(), product.getName());
        assertEquals(productDTO.getAvailable(), product.getAvailable());
        assertEquals(productDTO.getLeadTime(), product.getLeadTime());
        assertEquals(productDTO.getType(), product.getType());
        assertEquals(productDTO.getExpiryDate(), product.getExpiryDate());
        assertEquals(productDTO.getSeasonStartDate(), product.getSeasonStartDate());
        assertEquals(productDTO.getSeasonEndDate(), product.getSeasonEndDate());
    }
}
