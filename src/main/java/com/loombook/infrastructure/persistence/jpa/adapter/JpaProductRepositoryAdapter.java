package com.loombook.infrastructure.persistence.jpa.adapter;

import com.loombook.domain.model.Price;
import com.loombook.domain.model.Product;
import com.loombook.domain.model.ProductId;
import com.loombook.domain.model.ProductName;
import com.loombook.domain.port.ProductRepository;
import com.loombook.infrastructure.persistence.jpa.entity.ProductEntity;
import com.loombook.infrastructure.persistence.jpa.repository.SpringDataProductJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * ProductRepository 的 JPA 實作
 */
@Repository
public class JpaProductRepositoryAdapter implements ProductRepository {

    private final SpringDataProductJpaRepository jpaRepository;

    public JpaProductRepositoryAdapter(SpringDataProductJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Product save(Product product) {
        ProductEntity entity = toEntity(product);
        ProductEntity saved = jpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<Product> findById(ProductId id) {
        return jpaRepository.findById(id.value()).map(this::toDomain);
    }

    private ProductEntity toEntity(Product product) {
        return new ProductEntity(
                product.id().value(),
                product.name().value(),
                product.price().priceCents()
        );
    }

    private Product toDomain(ProductEntity entity) {
        return new Product(
                new ProductId(entity.getId()),
                new ProductName(entity.getName()),
                new Price(entity.getPriceCents())
        );
    }
}
