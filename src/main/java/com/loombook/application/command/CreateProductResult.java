package com.loombook.application.command;

/**
 * 建立商品結果 DTO
 */
public record CreateProductResult(String id, String name, int priceCents) {
}
