package com.loombook.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Value Objects 不變性測試
 */
class ValueObjectsTest {

    @Nested
    @DisplayName("ProductId 測試")
    class ProductIdTest {

        @Test
        @DisplayName("建立有效的 ProductId")
        void shouldCreateValidProductId() {
            ProductId id = new ProductId("prod-123");
            assertEquals("prod-123", id.value());
        }

        @Test
        @DisplayName("null 值應拋出 IllegalArgumentException")
        void shouldThrowExceptionForNullValue() {
            assertThrows(IllegalArgumentException.class, () -> new ProductId(null));
        }

        @Test
        @DisplayName("空白值應拋出 IllegalArgumentException")
        void shouldThrowExceptionForBlankValue() {
            assertThrows(IllegalArgumentException.class, () -> new ProductId(""));
            assertThrows(IllegalArgumentException.class, () -> new ProductId("   "));
        }
    }

    @Nested
    @DisplayName("ProductName 測試")
    class ProductNameTest {

        @Test
        @DisplayName("建立有效的 ProductName")
        void shouldCreateValidProductName() {
            ProductName name = new ProductName("Java 程式設計");
            assertEquals("Java 程式設計", name.value());
        }

        @Test
        @DisplayName("null 值應拋出 IllegalArgumentException")
        void shouldThrowExceptionForNullValue() {
            assertThrows(IllegalArgumentException.class, () -> new ProductName(null));
        }

        @Test
        @DisplayName("空白值應拋出 IllegalArgumentException")
        void shouldThrowExceptionForBlankValue() {
            assertThrows(IllegalArgumentException.class, () -> new ProductName(""));
            assertThrows(IllegalArgumentException.class, () -> new ProductName("   "));
        }
    }

    @Nested
    @DisplayName("Price 測試")
    class PriceTest {

        @Test
        @DisplayName("建立有效的 Price")
        void shouldCreateValidPrice() {
            Price price = new Price(9900);
            assertEquals(9900, price.priceCents());
        }

        @Test
        @DisplayName("零值應該有效")
        void shouldAllowZeroPrice() {
            Price price = new Price(0);
            assertEquals(0, price.priceCents());
        }

        @Test
        @DisplayName("負數應拋出 IllegalArgumentException")
        void shouldThrowExceptionForNegativeValue() {
            assertThrows(IllegalArgumentException.class, () -> new Price(-1));
            assertThrows(IllegalArgumentException.class, () -> new Price(-100));
        }
    }
}
