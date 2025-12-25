package com.loombook.domain.model;

/**
 * 商品價格 Value Object（以分為單位）
 */
public record Price(int priceCents) {
    public Price {
        if (priceCents < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
    }
}
