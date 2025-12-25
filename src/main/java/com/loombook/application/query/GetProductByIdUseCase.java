package com.loombook.application.query;

import com.loombook.domain.model.ProductId;
import com.loombook.domain.port.ProductRepository;

import java.util.Optional;

/**
 * 查詢商品用例
 */
public class GetProductByIdUseCase {

    private final ProductRepository productRepository;

    public GetProductByIdUseCase(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Optional<ProductResult> execute(String id) {
        return productRepository.findById(new ProductId(id))
                .map(product -> new ProductResult(
                        product.id().value(),
                        product.name().value(),
                        product.price().priceCents()
                ));
    }
}
