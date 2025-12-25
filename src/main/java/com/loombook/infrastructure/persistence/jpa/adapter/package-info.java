/**
 * Infrastructure JPA Adapter Layer - 基礎設施 JPA 適配器層
 * 
 * <p>本包包含 Domain Port 的 JPA 實作：
 * <ul>
 *   <li>實作 Domain Port（如 ProductRepository）的 Adapter</li>
 *   <li>負責 Domain Model 與 JPA Entity 之間的映射</li>
 * </ul>
 * 
 * <p><strong>架構約束：</strong>
 * <ul>
 *   <li>必須實作 Domain Port 介面</li>
 *   <li>可以注入 Spring Data JPA Repository</li>
 *   <li>負責 Domain ↔ Entity 的雙向轉換</li>
 *   <li>對外只暴露 Domain 類型</li>
 * </ul>
 */
package com.loombook.infrastructure.persistence.jpa.adapter;
