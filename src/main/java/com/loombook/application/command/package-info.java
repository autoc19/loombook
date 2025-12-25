/**
 * Application Command Layer - 應用命令層
 * 
 * <p>本包包含改變系統狀態的用例（Use Cases）：
 * <ul>
 *   <li>Command Use Cases（命令用例）</li>
 *   <li>Command DTOs（命令資料傳輸物件）</li>
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
package com.loombook.application.command;
