package com.loombook.domain.model;

/**
 * 商品名稱 Value Object
 */
public record ProductName(String value) {
    public ProductName {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("ProductName cannot be null or blank");
        }
    }
}
