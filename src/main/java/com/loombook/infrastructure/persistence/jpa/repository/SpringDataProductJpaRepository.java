package com.loombook.infrastructure.persistence.jpa.repository;

import com.loombook.infrastructure.persistence.jpa.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA Repository
 */
public interface SpringDataProductJpaRepository extends JpaRepository<ProductEntity, String> {
}
