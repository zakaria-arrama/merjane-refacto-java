package com.nimbleways.springboilerplate.mappers;

import com.nimbleways.springboilerplate.dto.product.ProductDTO;
import com.nimbleways.springboilerplate.entities.Product;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    // Mapping ProductDTO to Product entity
    Product toEntity(ProductDTO productDTO);

    // Mapping Product entity to ProductDTO
    ProductDTO toDTO(Product product);

}
