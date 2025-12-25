package com.loombook.domain.port;

import com.loombook.domain.model.Product;
import com.loombook.domain.model.ProductId;

import java.util.Optional;

/**
 * 商品 Repository 介面（Domain Port）
 * 
 * <p>定義持久化契約，由 Infrastructure 層實作。
 */
public interface ProductRepository {

    /**
     * 儲存商品
     * @param product 要儲存的商品
     * @return 儲存後的商品
     */
    Product save(Product product);

    /**
     * 依 ID 查詢商品
     * @param id 商品 ID
     * @return 商品（如存在）
     */
    Optional<Product> findById(ProductId id);
}
