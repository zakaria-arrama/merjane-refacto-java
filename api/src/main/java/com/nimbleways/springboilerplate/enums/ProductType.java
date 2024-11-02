package com.nimbleways.springboilerplate.enums;

public enum ProductType {

    NORMAL(1),

    SEASONAL(2),

    EXPIRABLE(3);

    private final int id;

    ProductType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static ProductType fromId(int id) {
        for (ProductType type : ProductType.values()) {
            if (type.getId() == id) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown ID: " + id);
    }

}

