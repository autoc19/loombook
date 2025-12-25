package com.loombook.domain.model;

/**
 * 商品唯一識別碼 Value Object
 */
public record ProductId(String value) {
    public ProductId {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("ProductId cannot be null or blank");
        }
    }
}
