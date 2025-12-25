package com.loombook.domain.model;

/**
 * 商品聚合根
 */
public record Product(ProductId id, ProductName name, Price price) {
    public Product {
        if (id == null) {
            throw new IllegalArgumentException("Product id cannot be null");
        }
        if (name == null) {
            throw new IllegalArgumentException("Product name cannot be null");
        }
        if (price == null) {
            throw new IllegalArgumentException("Product price cannot be null");
        }
    }
}
