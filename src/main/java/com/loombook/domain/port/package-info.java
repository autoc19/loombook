/**
 * Domain Port Layer - 領域埠層
 * 
 * <p>本包定義領域與外部世界的介面契約：
 * <ul>
 *   <li>Repository 介面（持久化埠）</li>
 *   <li>外部服務介面（如需要）</li>
 * </ul>
 * 
 * <p><strong>架構約束：</strong>
 * <ul>
 *   <li>只定義介面，不包含實作</li>
 *   <li>不得依賴 Spring Framework</li>
 *   <li>不得依賴具體技術（JPA、HTTP 等）</li>
 *   <li>介面方法只能使用 Domain 類型</li>
 * </ul>
 */
package com.loombook.domain.port;
