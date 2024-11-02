package com.nimbleways.springboilerplate.controllers;

import com.nimbleways.springboilerplate.dto.product.ProcessOrderResponse;
import com.nimbleways.springboilerplate.entities.Order;
import com.nimbleways.springboilerplate.entities.Product;
import com.nimbleways.springboilerplate.enums.ProductType;
import com.nimbleways.springboilerplate.exceptions.OrderNotFoundException;
import com.nimbleways.springboilerplate.services.implementations.ProductService;

import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrdersController {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrdersController.class);

    private ProductService productService;


    public OrdersController(ProductService productService) {
        this.productService = productService;
    }


    @PostMapping("{orderId}/processOrder")
    public ResponseEntity<ProcessOrderResponse> processOrder(@PathVariable Long orderId) {
        LOGGER.info("Starting to process order with ID: {}", orderId);
        Order order;
        try {
            order = productService.findOrderById(orderId);
        } catch (OrderNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        Set<Product> products = order.getItems();
        for (Product p : products) {
            if (ProductType.NORMAL == p.getType()){
                productService.handleNormalProduct(p);
            } else if (ProductType.SEASONAL == p.getType()) {
                productService.handleSeasonalProduct(p);
            } else if (p.getType() == ProductType.EXPIRABLE) {
                productService.handleExpirableProduct(p);
            }
        }

        return new ResponseEntity<>(new ProcessOrderResponse(order.getId()), HttpStatus.OK);
    }


}
