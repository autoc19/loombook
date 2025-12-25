package com.loombook.application.query;

import com.loombook.domain.model.Price;
import com.loombook.domain.model.Product;
import com.loombook.domain.model.ProductId;
import com.loombook.domain.model.ProductName;
import com.loombook.domain.port.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * GetProductByIdUseCase 測試
 */
class GetProductByIdUseCaseTest {

    private ProductRepository productRepository;
    private GetProductByIdUseCase useCase;

    @BeforeEach
    void setUp() {
        productRepository = mock(ProductRepository.class);
        useCase = new GetProductByIdUseCase(productRepository);
    }

    @Test
    @DisplayName("商品存在時應回傳商品資訊")
    void shouldReturnProductWhenExists() {
        Product product = new Product(
                new ProductId("prod-123"),
                new ProductName("Java 程式設計"),
                new Price(9900)
        );
        when(productRepository.findById(new ProductId("prod-123"))).thenReturn(Optional.of(product));

        Optional<ProductResult> result = useCase.execute("prod-123");

        assertTrue(result.isPresent());
        assertEquals("prod-123", result.get().id());
        assertEquals("Java 程式設計", result.get().name());
        assertEquals(9900, result.get().priceCents());
    }

    @Test
    @DisplayName("商品不存在時應回傳空")
    void shouldReturnEmptyWhenNotExists() {
        when(productRepository.findById(new ProductId("not-exist"))).thenReturn(Optional.empty());

        Optional<ProductResult> result = useCase.execute("not-exist");

        assertTrue(result.isEmpty());
    }
}
