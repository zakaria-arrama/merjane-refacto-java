package com.nimbleways.springboilerplate.enums;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProductTypeTests {

    @Test
    void testGetId() {
        assertEquals(1, ProductType.NORMAL.getId(), "NORMAL should have ID 1");
        assertEquals(2, ProductType.SEASONAL.getId(), "SEASONAL should have ID 2");
        assertEquals(3, ProductType.EXPIRABLE.getId(), "EXPIRABLE should have ID 3");
    }

    @Test
    void testFromId() {
        assertEquals(ProductType.NORMAL, ProductType.fromId(1), "ID 1 should return NORMAL");
        assertEquals(ProductType.SEASONAL, ProductType.fromId(2), "ID 2 should return SEASONAL");
        assertEquals(ProductType.EXPIRABLE, ProductType.fromId(3), "ID 3 should return EXPIRABLE");
    }

    @Test
    void testFromIdInvalid() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> ProductType.fromId(4));
        assertEquals("Unknown ID: 4", exception.getMessage(), "Exception message should indicate unknown ID");
    }

}
