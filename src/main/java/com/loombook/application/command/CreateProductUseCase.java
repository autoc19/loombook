package com.loombook.application.command;

import com.loombook.domain.model.Price;
import com.loombook.domain.model.Product;
import com.loombook.domain.model.ProductId;
import com.loombook.domain.model.ProductName;
import com.loombook.domain.port.ProductRepository;

import java.util.UUID;

/**
 * 建立商品用例
 */
public class CreateProductUseCase {

    private final ProductRepository productRepository;

    public CreateProductUseCase(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public CreateProductResult execute(CreateProductCommand command) {
        ProductId id = new ProductId(UUID.randomUUID().toString());
        ProductName name = new ProductName(command.name());
        Price price = new Price(command.priceCents());

        Product product = new Product(id, name, price);
        Product saved = productRepository.save(product);

        return new CreateProductResult(
                saved.id().value(),
                saved.name().value(),
                saved.price().priceCents()
        );
    }
}
