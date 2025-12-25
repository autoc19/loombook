/**
 * Infrastructure JPA Repository Layer - 基礎設施 JPA Repository 層
 * 
 * <p>本包包含 Spring Data JPA Repository：
 * <ul>
 *   <li>Spring Data JPA Repository 介面</li>
 *   <li>繼承 JpaRepository 的介面</li>
 * </ul>
 * 
 * <p><strong>架構約束：</strong>
 * <ul>
 *   <li>只能被同層的 Adapter 使用</li>
 *   <li>不得被 Controller 或 UseCase 直接注入</li>
 *   <li>屬於 Infrastructure 內部元件</li>
 * </ul>
 */
package com.loombook.infrastructure.persistence.jpa.repository;
