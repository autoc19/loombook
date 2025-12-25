/**
 * API REST Layer - REST API 層
 * 
 * <p>本包包含 REST 端點：
 * <ul>
 *   <li>REST Controller（@RestController）</li>
 *   <li>Request/Response DTOs</li>
 *   <li>Exception Handler（如需要）</li>
 * </ul>
 * 
 * <p><strong>架構約束：</strong>
 * <ul>
 *   <li>只能依賴 Application 層（UseCase）</li>
 *   <li>不得直接依賴 Infrastructure 層</li>
 *   <li>不得直接呼叫 Repository</li>
 *   <li>Controller 只負責 HTTP 協議轉換</li>
 * </ul>
 */
package com.loombook.api.rest;
