package com.nimbleways.springboilerplate.services.implementations;

import java.time.LocalDate;

import com.nimbleways.springboilerplate.entities.Order;
import com.nimbleways.springboilerplate.exceptions.OrderNotFoundException;
import com.nimbleways.springboilerplate.exceptions.ProductNotFoundException;
import com.nimbleways.springboilerplate.repositories.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.nimbleways.springboilerplate.entities.Product;
import com.nimbleways.springboilerplate.repositories.ProductRepository;

/**
 * Service class for managing Product entities.
 * Provides methods for creating, updating, and deleting products.
 */
@Service
public class ProductService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductService.class);


    ProductRepository productRepository;

    NotificationService notificationService;

    private OrderRepository orderRepository;

    public ProductService(ProductRepository productRepository, NotificationService notificationService, OrderRepository orderRepository) {
        this.productRepository = productRepository;
        this.notificationService = notificationService;
        this.orderRepository = orderRepository;
    }


    public void notifyDelay(int leadTime, Product p) {
        p.setLeadTime(leadTime);
        productRepository.save(p);
        notificationService.sendDelayNotification(leadTime, p.getName());
    }


    public Order findOrderById(Long orderId) throws OrderNotFoundException {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> {
                    LOGGER.error("Order with ID {} not found", orderId);
                    return new OrderNotFoundException("Order with ID " + orderId + " not found");
                });
    }

    /**
     * Handles the processing of an expirable product. If the product is available and has not expired,
     * it decrements the stock and allows the sale. If the product has expired or is out of stock,
     * it sets availability to zero and sends a notification to inform customers of its unavailability.
     *
     * @param p the expirable product to process
     */
    public void handleExpirableProduct(Product p) {
        if (p.getAvailable() > 0 && p.getExpiryDate().isAfter(LocalDate.now())) {
            // Product is available and not expired, decrement stock
            p.setAvailable(p.getAvailable() - 1);
            productRepository.save(p);
        } else {
            // Product is either out of stock or expired
            notificationService.sendExpirationNotification(p.getName(), p.getExpiryDate());
            p.setAvailable(0);  // Ensure availability is set to zero
            productRepository.save(p);
        }
    }

    /**
     * Handles the processing of a seasonal product. If the current date is within the season
     * and the product is available, it decrements the stock. If the product is not yet in season,
     * or if its lead time exceeds the end of the season, it sends an out-of-stock notification.
     * If the product will be available before the season ends, it notifies customers of the delay.
     *
     * @param p the seasonal product to process
     */
    public void handleSeasonalProduct(Product p) {
        LocalDate now = LocalDate.now();

        // Check if the product is in season and available
        if (now.isAfter(p.getSeasonStartDate()) && now.isBefore(p.getSeasonEndDate()) && p.getAvailable() > 0) {
            p.setAvailable(p.getAvailable() - 1);
            productRepository.save(p);
        }
        // Out of stock and lead time extends past the season
        else if (now.plusDays(p.getLeadTime()).isAfter(p.getSeasonEndDate())) {
            notificationService.sendOutOfStockNotification(p.getName());
            p.setAvailable(0);
            productRepository.save(p);
        }
        // Not yet in season
        else if (p.getSeasonStartDate().isAfter(now)) {
            notificationService.sendOutOfStockNotification(p.getName());
        }
        // Notify delay for other cases
        else {
            notifyDelay(p.getLeadTime(), p);
        }

    }

    /**
     * Handles the processing of a normal product. If the product is available, it decrements
     * the stock and allows the sale. If the product is out of stock and has a lead time,
     * it notifies customers of the delay.
     *
     * @param p the normal product to process
     */
    public void handleNormalProduct(Product p) {
        if (p.getAvailable() > 0) {
            p.setAvailable(p.getAvailable() - 1);
            productRepository.save(p);
        } else if (p.getLeadTime() > 0) {
            notifyDelay(p.getLeadTime(), p);
        }
    }

    /**
     * Creates a new product.
     *
     * @param product the product entity to be created
     * @return the created product entity with an assigned ID
     */
    public Product createProduct(Product product) {
        LOGGER.info("Creating new product with name: {}", product.getName());
        Product createdProduct = productRepository.save(product);
        LOGGER.info("Product created with ID: {}", createdProduct.getId());
        return createdProduct;
    }

    /**
     * Updates an existing product by ID.
     * If the product with the specified ID does not exist, an exception is thrown.
     *
     * @param productId      the ID of the product to update
     * @param updatedProduct the product entity containing updated details
     * @return the updated product entity
     * @throws RuntimeException if the product with the specified ID is not found
     */
    public Product updateProduct(Long productId, Product updatedProduct) throws ProductNotFoundException {
        LOGGER.info("Updating product with ID: {}", productId);


        Product product = productRepository.findById(productId)
                .orElseThrow(() -> {
                    LOGGER.error("Product with ID {} not found for update", productId);
                    return new ProductNotFoundException("Product not found");
                });
        product.setName(updatedProduct.getName());
        product.setAvailable(updatedProduct.getAvailable());
        product.setExpiryDate(updatedProduct.getExpiryDate());
        product.setLeadTime(updatedProduct.getLeadTime());
        product.setType(updatedProduct.getType());
        Product savedProduct = productRepository.save(product);
        LOGGER.info("Product with ID: {} updated successfully", productId);
        return savedProduct;
    }

}
