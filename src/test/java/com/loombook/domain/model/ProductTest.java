package com.loombook.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Product 聚合根測試
 */
class ProductTest {

    @Test
    @DisplayName("建立有效的 Product")
    void shouldCreateValidProduct() {
        ProductId id = new ProductId("prod-123");
        ProductName name = new ProductName("Java 程式設計");
        Price price = new Price(9900);

        Product product = new Product(id, name, price);

        assertEquals(id, product.id());
        assertEquals(name, product.name());
        assertEquals(price, product.price());
    }

    @Test
    @DisplayName("Product 必須持有所有必要屬性")
    void shouldHaveAllRequiredProperties() {
        Product product = new Product(
                new ProductId("prod-456"),
                new ProductName("Spring Boot 實戰"),
                new Price(12000)
        );

        assertNotNull(product.id());
        assertNotNull(product.name());
        assertNotNull(product.price());
    }

    @Test
    @DisplayName("透過 Product 建立時 Value Object 約束應生效")
    void shouldEnforceValueObjectConstraints() {
        assertThrows(IllegalArgumentException.class, () ->
                new Product(new ProductId(""), new ProductName("Test"), new Price(100)));

        assertThrows(IllegalArgumentException.class, () ->
                new Product(new ProductId("id"), new ProductName(""), new Price(100)));

        assertThrows(IllegalArgumentException.class, () ->
                new Product(new ProductId("id"), new ProductName("Test"), new Price(-1)));
    }
}
