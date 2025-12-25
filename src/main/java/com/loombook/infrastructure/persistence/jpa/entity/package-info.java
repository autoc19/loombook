/**
 * Infrastructure JPA Entity Layer - 基礎設施 JPA 實體層
 * 
 * <p>本包包含 JPA 持久化實體：
 * <ul>
 *   <li>JPA Entity（@Entity 標註的類別）</li>
 *   <li>Entity 與 Domain Model 的映射邏輯</li>
 * </ul>
 * 
 * <p><strong>架構約束：</strong>
 * <ul>
 *   <li>Entity 只用於持久化，不暴露給 Application 或 API 層</li>
 *   <li>Domain Model 與 Entity 的轉換在 Adapter 中進行</li>
 * </ul>
 */
package com.loombook.infrastructure.persistence.jpa.entity;
