package com.loombook.api.rest;

/**
 * 建立商品請求 DTO
 */
public record CreateProductRequest(String name, int priceCents) {
}
