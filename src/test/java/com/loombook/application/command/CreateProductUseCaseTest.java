package com.loombook.application.command;

import com.loombook.domain.model.Product;
import com.loombook.domain.port.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * CreateProductUseCase 測試
 */
class CreateProductUseCaseTest {

    private ProductRepository productRepository;
    private CreateProductUseCase useCase;

    @BeforeEach
    void setUp() {
        productRepository = mock(ProductRepository.class);
        useCase = new CreateProductUseCase(productRepository);
    }

    @Test
    @DisplayName("輸入合法時應建立商品並回傳結果")
    void shouldCreateProductWhenInputIsValid() {
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CreateProductCommand command = new CreateProductCommand("Java 程式設計", 9900);
        CreateProductResult result = useCase.execute(command);

        assertNotNull(result.id());
        assertEquals("Java 程式設計", result.name());
        assertEquals(9900, result.priceCents());

        ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);
        verify(productRepository).save(captor.capture());
        assertEquals("Java 程式設計", captor.getValue().name().value());
    }

    @Test
    @DisplayName("name 空白時應拋出例外")
    void shouldThrowExceptionWhenNameIsBlank() {
        CreateProductCommand command = new CreateProductCommand("", 9900);
        assertThrows(IllegalArgumentException.class, () -> useCase.execute(command));
    }

    @Test
    @DisplayName("priceCents 為負數時應拋出例外")
    void shouldThrowExceptionWhenPriceIsNegative() {
        CreateProductCommand command = new CreateProductCommand("Test", -1);
        assertThrows(IllegalArgumentException.class, () -> useCase.execute(command));
    }
}
