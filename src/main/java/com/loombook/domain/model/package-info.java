/**
 * Domain Model Layer - 領域模型層
 * 
 * <p>本包包含領域核心概念：
 * <ul>
 *   <li>Aggregate Root（聚合根）</li>
 *   <li>Value Objects（值物件）</li>
 *   <li>Domain Events（領域事件，如需要）</li>
 * </ul>
 * 
 * <p><strong>架構約束：</strong>
 * <ul>
 *   <li>不得依賴 Spring Framework</li>
 *   <li>不得依賴 JPA/Hibernate</li>
 *   <li>不得依賴 Web 層</li>
 *   <li>必須保持框架無關</li>
 * </ul>
 */
package com.loombook.domain.model;
