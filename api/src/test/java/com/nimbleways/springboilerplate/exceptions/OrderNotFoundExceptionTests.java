package com.nimbleways.springboilerplate.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderNotFoundExceptionTests {

    @Test
    void testMessageOnlyConstructor() {
        String message = "Order not found";
        OrderNotFoundException exception = new OrderNotFoundException(message);

        assertEquals(message, exception.getMessage(), "The exception message should match the input message");
    }

    @Test
    void testMessageAndCauseConstructor() {
        String message = "Order not found";
        Throwable cause = new RuntimeException("Database connection error");
        OrderNotFoundException exception = new OrderNotFoundException(message, cause);

        assertEquals(message, exception.getMessage(), "The exception message should match the input message");
        assertEquals(cause, exception.getCause(), "The exception cause should match the input cause");
    }

}
