package com.loombook.infrastructure.persistence.jpa.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * 商品 JPA 實體
 */
@Entity
@Table(name = "products")
public class ProductEntity {

    @Id
    private String id;
    private String name;
    private int priceCents;

    protected ProductEntity() {
    }

    public ProductEntity(String id, String name, int priceCents) {
        this.id = id;
        this.name = name;
        this.priceCents = priceCents;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPriceCents() {
        return priceCents;
    }
}
