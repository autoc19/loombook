# Product Steering

## 1. 背景與願景

本專案是「企業級線上書店與交易平台」的後端系統。短期目標不是一次完成所有企業級能力，而是以可驗收的最小閉環（Level 0）建立可演進的工程骨架，作為後續等級（Level 1+）擴展的基礎。

核心願景：構建一個 **高併發、高一致性、可追溯** 的企業級交易平台後端。

## 2. Level 0 的產品目標

Level 0 的交付物聚焦在：

- 可運行、可測試、可演進的最小系統骨架
- 驗證六角架構/DDD 邊界與依賴方向
- 建立強制 TDD 的交付紀律

Level 0 必須提供的最小業務閉環：

- 商品建立
- 依商品 ID 查詢
- 最小可觀測性門檻：健康檢查（`/actuator/health`）

## 3. 使用者/角色（Level 0）

Level 0 僅需要覆蓋最小角色敘事，用於界定 API 行為：

- 平台呼叫端（可視為後台/管理端或整合端）建立商品
- 平台呼叫端以 `id` 查詢商品

## 4. 範圍（Scope）

### 4.1 Level 0 需要做（必做）

- 商品資料必須真實持久化到資料庫（H2 in-memory + JPA），不得只存於記憶體 Map
- 嚴格遵守六角架構分層（Domain / Application / Infrastructure / API），Domain 必須完全框架無關
- 強制 TDD：每個能力必須先寫測試再寫實作，且遵循固定開發順序
- 提供最小 ArchUnit 守門規則（至少兩條）

### 4.2 Level 0 不做（明確禁止）

- 身分與權限：不得引入 Spring Security/OAuth2/RBAC/多租戶
- 訂單/支付/庫存扣減/防超賣/Saga 等交易能力
- Kafka、Structured Concurrency、Testcontainers、E2E、壓測報告、OpenAPI 檔案
- 商品詳情聚合查詢（並行拉取庫存/評論/促銷）、降級、快速失敗等 Level 1+ 能力

## 5. 成功標準（Definition of Done）

- API 契約不得偏離 Level 0 FRS：
  - `POST /api/products`：201/400
  - `GET /api/products/{id}`：200/404
  - `GET /actuator/health`：200（UP）
- 可執行並通過：
  - `./gradlew test`
  - `./gradlew bootRun`（監聽 8080）
- 架構合規：
  - Domain 不依賴 Spring
  - API 不依賴 Infrastructure
- 測試合規：每個能力至少具備
  - Domain 測試（VO 不變性）
  - UseCase 測試（CreateProduct / GetProductById）
  - API 測試（201/400/200/404）

## 6. 風險與邊界

- Level 0 的「企業級」主要體現在工程邊界與可演進性（架構、測試、依賴方向），而非功能完整度。
- 任何超出 Level 0 FRS 的擴展（新增端點/新增模組/引入未要求框架）一律視為偏離範圍。
