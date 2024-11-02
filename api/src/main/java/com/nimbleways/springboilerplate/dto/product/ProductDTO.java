package com.nimbleways.springboilerplate.dto.product;

import com.nimbleways.springboilerplate.enums.ProductType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private Long id;
    private Integer leadTime;
    private Integer available;
    private ProductType type;
    private String name;
    private LocalDate expiryDate;
    private LocalDate seasonStartDate;
    private LocalDate seasonEndDate;

}
