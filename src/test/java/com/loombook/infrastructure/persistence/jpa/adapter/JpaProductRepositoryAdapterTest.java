package com.loombook.infrastructure.persistence.jpa.adapter;

import com.loombook.domain.model.Price;
import com.loombook.domain.model.Product;
import com.loombook.domain.model.ProductId;
import com.loombook.domain.model.ProductName;
import com.loombook.domain.port.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JpaProductRepositoryAdapter 整合測試
 */
@SpringBootTest
class JpaProductRepositoryAdapterTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    @DisplayName("應能儲存並查回商品")
    void shouldSaveAndFindProduct() {
        Product product = new Product(
                new ProductId("test-123"),
                new ProductName("測試商品"),
                new Price(5000)
        );

        Product saved = productRepository.save(product);

        assertEquals("test-123", saved.id().value());
        assertEquals("測試商品", saved.name().value());
        assertEquals(5000, saved.price().priceCents());

        Optional<Product> found = productRepository.findById(new ProductId("test-123"));

        assertTrue(found.isPresent());
        assertEquals("test-123", found.get().id().value());
        assertEquals("測試商品", found.get().name().value());
        assertEquals(5000, found.get().price().priceCents());
    }

    @Test
    @DisplayName("查詢不存在的商品應回傳空")
    void shouldReturnEmptyWhenNotFound() {
        Optional<Product> found = productRepository.findById(new ProductId("not-exist"));
        assertTrue(found.isEmpty());
    }
}
