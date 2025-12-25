以下為「**可直接交給 IDE AI 實作**」的 **完整 Level 0 規範文檔（Spring Boot 黃金路徑：H2 + Spring Data JPA + 六角/DDD + 強制 TDD + 最小 ArchUnit）**。本版本同時保留企業級目標敘事，但把落地範圍縮到 0 級可驗收的最小閉環。

***

# 企業級線上書店與交易平台 - 功能需求規格書 (FRS)（gpt.dev.v7 / Level 0）

> 文檔用途：作為專案的「合同」與 AI 行為邊界。實作必須嚴格遵循本文，不得擅自擴展需求或引入未要求的模組。
> 技術主線：Java 21 + Spring Boot 3.4.13 + Spring MVC（同步）+ Virtual Threads + H2（in-memory）+ Spring Data JPA + 六角架構 + DDD（最小集合）+ 強制 TDD + 最小 ArchUnit。

***

## 1. 專案背景與目標（Level 0）
本專案旨在構建一個**高併發、高一致性、可追溯**的企業級後端交易平台。Level 0 的目標是交付一個可運行、可測試、可演進的最小閉環骨架，驗證工程腳手架、分層邊界與開發規範是否成立。

**核心商業價值（Level 0 最小可驗證子集）**：
- **極致效能（基礎就位）**：必須啟用 Java 21 Virtual Threads，保持同步 Spring MVC 風格並禁止 WebFlux/Reactor 混搭，確保並發模型單一。
- **數據與資金安全（習慣先行）**：Level 0 不做訂單/支付，但必須落地 DDD 不變性與真實持久化（H2 + JPA），用 TDD 形成「先驗收再開發」的紀律。
- **可觀測性（最小門檻）**：必須提供 `/actuator/health` 作為可運維能力的最小起點（Tracing/Metrics/Logs 全量留到後續等級）。

***

## 2. 核心功能模組需求（Level 0）
> Level 0 只落地 2.2 的最小子集；2.1/2.3/2.4 只保留敘事與約束，不實作完整能力。

### 2.1 用戶與權限管理 (Identity & Access Management)（Level 0：不實作）
- **功能描述**：提供安全的用戶認證與細粒度的權限控制（OAuth2 / RBAC / 租戶隔離）。
- **Level 0 約束**：不得引入 Spring Security/OAuth2 的任何配置或端點，避免 AI 發散與過度設計。

### 2.2 商品目錄與查詢 (Product Catalog & Discovery)（Level 0：實作最小閉環）
- **功能描述（Level 0 實作）**：提供商品的建立與按 ID 查詢，作為流量入口的最小可用版本。
- **業務規則（Level 0 實作）**：
- 商品資料必須持久化到 H2（in-memory），不可只存在記憶體 Map。
- 不做“商品詳情聚合查詢（並行拉庫存/評論/促銷）”與降級/快速失敗機制（保留 Level 1+）。
- 不做 P95 < 100ms 性能指標驗收（Level 0 只驗收可運行與可測試）。

### 2.3 訂單與支付核心 (Order & Payment Core)（Level 0：不實作）
- **功能描述**：支付冪等、Saga 一致性、防超賣等交易核心能力保留到後續等級。
- **Level 0 約束**：不得實作或模擬支付/庫存扣減，避免伪複雜度污染入門閉環。

### 2.4 可觀測性與審計 (Observability & Audit)（Level 0：最小門檻）
- **功能描述（Level 0 實作）**：提供最小健康檢查能力。
- **業務規則（Level 0 實作）**：
- 必須啟用 Spring Boot Actuator 並確保 `GET /actuator/health` 可訪問且狀態為 UP（採用預設即可）。

***

## 3. 非功能性需求 (Non-Functional Requirements)（Level 0）
### 3.1 架構穩定性（Level 0）
- **并發主線**：同步程式碼風格 + Virtual Threads；禁止 WebFlux/Reactor。
- **分層穩定性**：必須採用六角架構（Domain / Application / Infrastructure / API），Domain 層完全獨立於框架。

### 3.2 可測試性與品質驗收（Level 0：強制 TDD）
- **測試先行（TDD）**：必須遵守 Red → Green → Refactor，且每個能力都必須先寫測試再寫實作。
- **架構守門**：必須提供 ArchUnit 測試，但 Level 0 僅實作 2 條核心規則（第 7.4 節）。

### 3.3 技術標準限制（Level 0）
- Java 21 (LTS)。
- Spring Boot 3.4.13（透過 `gradle.properties` 鎖定）。
- 必須啟用 Virtual Threads（`spring.threads.virtual.enabled: true`）。
- 資料庫：H2 in-memory + Spring Data JPA（以最小代碼量走 Spring Boot 黃金路徑，並貼近未來切 Postgres 的真實形態）。

***

## 4. 交付標準 (Definition of Done)（Level 0）
1. **API 契約（Level 0）**：本文第 6 節即為 API 契約；Level 0 不強制 OpenAPI 檔案，但不得偏離本文字段/路徑/錯誤碼。
2. **可運行（Level 0）**：必須可執行 `./gradlew test` 且全綠，並可 `./gradlew bootRun` 啟動監聽 8080。
3. **架構合規（Level 0）**：ArchUnit 最小 2 條規則必須通過（Domain 不依賴 Spring；API 不得依賴 Infrastructure）。
4. **TDD 合規（Level 0）**：每個用例至少具備 Domain 測試 + UseCase 測試 + API 測試，且實作順序必須先測試後實作（見第 7 節）。

***

## 5. 系統範圍（Level 0 做 / 不做）
### 5.1 Level 0 做
- 商品建立、商品查詢（真實落庫 H2）。
- Actuator health。
- 六角架構 + DDD 最小集合 + Repository Adapter 模式。
- 強制 TDD + 最小 ArchUnit。

### 5.2 Level 0 不做（明確禁止）
- OAuth2 / RBAC / 多租戶 TenantId / Scoped Values / Structured Concurrency / Kafka / Saga / Testcontainers / E2E / 壓測報告 / OpenAPI 檔案。

***

## 6. 功能需求（Level 0：商品閉環）
### 6.1 領域模型（DDD 最小集合）
必須具備以下 Domain 概念（麻雀雖小五臟俱全）：
- Aggregate Root：`Product`  
- Value Objects：
  - `ProductId(String value)`：value 不得為 null/空白  
  - `ProductName(String value)`：value 不得為 null/空白  
  - `Price(int priceCents)`：priceCents 必須 >= 0  
- Domain Port（Repository 介面）：`ProductRepository`（定義在 Domain，不得依賴 Spring）。

> Level 0 VO/DTO 建議用 `record + compact constructor` 表達不變性（符合 v7 的 Java 21 風格方向）。

### 6.2 Use Cases（Application 層）
- Command UseCase：CreateProduct  
- Query UseCase：GetProductById  

**硬規則**：
- Controller 只能呼叫 UseCase；不得直接呼叫任何 Repository。  
- UseCase 只能依賴 Domain Port（`ProductRepository`）；不得注入 `JpaRepository`。  

***

## 7. 架構與實作規範（Level 0：可控 AI 的關鍵）
### 7.1 六角架構包結構（必須存在）
以下 package 必須存在（可先空目錄 + `.gitkeep`）：
- `com.loombook.domain.model`
- `com.loombook.domain.port`
- `com.loombook.application.command`
- `com.loombook.application.query`
- `com.loombook.infrastructure.persistence.jpa.entity`
- `com.loombook.infrastructure.persistence.jpa.repository`
- `com.loombook.infrastructure.persistence.jpa.adapter`
- `com.loombook.api.rest`

### 7.2 Repository Adapter 模式（Level 0 強制）
必須按以下三段式落地（避免直接把 JPA 泄露到 Application）：
1. **Domain Port**：`domain.port.ProductRepository`  
2. **Spring Data Repository（內部元件）**：`infrastructure.persistence.jpa.repository.SpringDataProductJpaRepository`  
3. **Adapter**：`infrastructure.persistence.jpa.adapter.JpaProductRepositoryAdapter implements ProductRepository`  

並強制：JPA Repository 只能被 Adapter 使用（不要被 Controller 或 UseCase 直接注入）。

### 7.3 依赖方向（Level 0 強制）
- Domain 不得依賴 Spring / JPA / Web。
- API 只能依賴 Application，不得依賴 Infrastructure。
- Infrastructure 可依賴 Spring/JPA，並實作 Domain Port。

### 7.4 ArchUnit（Level 0 最小 2 條）
必須提供 `ArchitectureTest` 並至少驗證：
- Rule A：`com.loombook.domain..` 不得依賴 `org.springframework..`  
- Rule B：`com.loombook.api..` 不得依賴 `com.loombook.infrastructure..`

***

## 8. API 規格（契約即測試來源）
### 8.1 建立商品
- Method：POST  
- Path：`/api/products`  
- Request JSON：
  - `name`：string，required，非空白
  - `priceCents`：int，required，>= 0
- Response：
  - 201：`id`, `name`, `priceCents`
  - 400：參數不合法

### 8.2 查詢商品
- Method：GET  
- Path：`/api/products/{id}`  
- Response：
  - 200：`id`, `name`, `priceCents`
  - 404：找不到商品

### 8.3 健康檢查
- Method：GET  
- Path：`/actuator/health`  
- Response：200（UP）

***

## 9. TDD 規範（Level 0：先測試後開發）
### 9.1 TDD 原則（硬門禁）
- 必須遵循 Red → Green → Refactor。
- 禁止“先寫功能再補測試”；缺少測試視為未完成。

### 9.2 固定開發順序（AI 必須照做）
每個能力必須依序完成：  
1) Domain tests → 2) Domain code → 3) UseCase tests → 4) UseCase code → 5) API tests → 6) API code。

### 9.3 必須存在的測試集合
- Domain Unit Tests：VO 不變性（ProductId/ProductName/Price）。
- Application Tests：CreateProduct / GetProductById 行為（可用 SpringBootTest + H2 或 Test Double，但必須穩定）。  
- API Tests：201/400/200/404。  

### 9.4 測試即規格
- 所有測試名稱需能對應到第 8 節 API 契約或第 6 節 Domain 規則。  
- 測試不得依賴外部服務（H2 in-memory 允許）。  

---

## 10. 工程配置與依賴（Level 0 必須）
### 10.1 Gradle 依賴（必須包含）
- `spring-boot-starter-web`
- `spring-boot-starter-data-jpa`
- `com.h2database:h2`
- `spring-boot-starter-actuator`
- `spring-boot-starter-test`
- `com.tngtech.archunit:archunit-junit5`（或等價）

### 10.2 application.yml（必須包含）
- `server.port: 8080`  
- `spring.threads.virtual.enabled: true`（Virtual Threads 開啟）
- H2 datasource + JPA 最小配置（允許 `ddl-auto: create-drop` 作為 Level 0）  
- Actuator health（預設即可）

***

## 11. 必須產出的檔案清單（AI 交付物）
### 11.1 必須檔案
- `build.gradle.kts`, `gradle.properties`, `settings.gradle.kts`, `gradlew*`。
- `src/main/resources/application.yml`。
- `src/main/java/com/loombook/LoombookApplication.java`。
- Domain：
  - `domain/model/Product`
  - `domain/model/ProductId`
  - `domain/model/ProductName`
  - `domain/model/Price`
  - `domain/port/ProductRepository`
- Infrastructure：
  - `infrastructure/persistence/jpa/entity/ProductEntity`
  - `infrastructure/persistence/jpa/repository/SpringDataProductJpaRepository`
  - `infrastructure/persistence/jpa/adapter/JpaProductRepositoryAdapter`
- Application：
  - `application/command/CreateProductUseCase`
  - `application/query/GetProductByIdUseCase`
- API：
  - `api/rest/ProductController`
  - DTO（request/response）使用 `record`
- Tests：
  - Domain tests
  - UseCase tests
  - API tests
  - `ArchitectureTest`（ArchUnit 2 條）

### 11.2 必須能跑通的命令
- `./gradlew test`  
- `./gradlew bootRun`

***

## 12. Level 0 → Level 1 迁移预留点（不实现，只做约束）
> 本节用于避免 Level 0 代码写死在 H2 上，保证未来切换到 Postgres 只需改配置与少量依赖，不改领域与用例。

- **DB 切换策略**：H2 → PostgreSQL  
  - 允许修改：datasource 配置、依赖、迁移工具（Liquibase/Flyway）  
  - 禁止修改：Domain 模型、Domain Port、UseCase 对 Port 的依赖方式（仍然只依赖 Domain Port）。
- **测试演进**：  
  - Level 0：H2 + SpringBootTest 为主  
  - Level 1：引入 Testcontainers Postgres（替换/新增集成测试），但保持 Domain 单元测试不变。
- **可观测性演进**：  
  - Level 0：仅 health  
  - Level 1：增加 metrics/log 格式规范（不在本等级实现）。

***

## 13. 给 IDE AI 的执行指令（最终版：防失控 + TDD）
把下面这段作为“唯一实现提示词”投喂给 IDE AI：

> 按 `specs/gpt.dev.v7.level0.frs.md` 实现 Level 0。  
> 强制 TDD：Red → Green → Refactor；输出顺序必须是“测试→实现”，且严格遵循：Domain tests → Domain code → UseCase tests → UseCase code → API tests → API code。  
> 数据层必须使用 H2 + Spring Data JPA，但必须保持六角边界：Domain 定义 ProductRepository Port；Infrastructure 内部使用 JpaRepository；Adapter 实现 Domain Port；Application 只能注入 Domain Port；Controller 只能依赖 Application。  
> 必须提供 Actuator health。  
> 禁止扩展：Security/OAuth2/Kafka/Testcontainers/多租户/Structured Concurrency/Tracing/性能压测/OpenAPI 文件。  
> 必须通过：`./gradlew test` 和 `./gradlew bootRun`。

--- 

如果你希望进一步提高 AI “一次成功率”，可以再补一条“**命名与路径强约束**”（例如固定类名/文件名/包名），让生成更可控；你要我把这些也固化进文档吗？