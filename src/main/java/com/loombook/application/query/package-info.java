/**
 * Application Query Layer - 應用查詢層
 * 
 * <p>本包包含讀取系統狀態的用例（Use Cases）：
 * <ul>
 *   <li>Query Use Cases（查詢用例）</li>
 *   <li>Query DTOs（查詢資料傳輸物件）</li>
 * </ul>
 * 
 * <p><strong>架構約束：</strong>
 * <ul>
 *   <li>只能依賴 Domain 層（domain.model, domain.port）</li>
 *   <li>不得直接依賴 Infrastructure 層</li>
 *   <li>不得注入 JpaRepository</li>
 *   <li>透過 Domain Port 與外部世界互動</li>
 * </ul>
 */
package com.loombook.application.query;
