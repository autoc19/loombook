package com.loombook.application.command;

/**
 * 建立商品命令 DTO
 */
public record CreateProductCommand(String name, int priceCents) {
}
