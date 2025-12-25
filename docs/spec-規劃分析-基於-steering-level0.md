# 基於 Steering 的 Spec 規劃分析（Level 0）

本文件根據 `.spec-workflow/steering/{product,tech,structure}.md` 與 `docs/企業級線上書店與交易平台0級mini-v2.md` 的 Level 0 約束，整理「為了完成 Level 0 最小閉環」所需建立的 spec 文檔集合，以及每個 spec 的實作任務清單（可直接轉寫為各 spec 的 `tasks.md`）。

## 0. 核心前提（必須遵守）

- 技術棧鎖定：Java 21、Spring Boot 3.4.13、Spring MVC（同步）、Virtual Threads（`spring.threads.virtual.enabled: true`）、H2 in-memory + Spring Data JPA、Actuator。
- 架構鎖定：六角架構（Domain / Application / Infrastructure / API），Domain 必須框架無關。
- 交付鎖定：強制 TDD（Red → Green → Refactor），且每個能力必須遵循固定順序：
  - Domain tests → Domain code → UseCase tests → UseCase code → API tests → API code
- 最小架構守門：ArchUnit 至少兩條規則：
  - Domain 不依賴 Spring
  - API 不依賴 Infrastructure
- 明確禁止：Security/OAuth2、訂單/支付、Kafka、Testcontainers、E2E、OpenAPI 檔案等（詳見 steering）。

## 1. 建議的 Spec 拆分（必要且最小）

為了降低任務耦合、保持每個 spec 可驗收且可演進，建議拆成 3 個 spec（皆採 kebab-case 命名）：

- spec A：`platform-bootstrap-level0`
- spec B：`architecture-guardrails-level0`
- spec C：`product-catalog-level0`

### 1.1 每個 spec 需要建立的文檔（spec-workflow 標準三件套）

對於每個 spec（A/B/C），都需要建立以下 3 份文檔：

- `.spec-workflow/specs/{spec-name}/requirements.md`
- `.spec-workflow/specs/{spec-name}/design.md`
- `.spec-workflow/specs/{spec-name}/tasks.md`

> 本分析文件只負責「規劃與任務清單」，實際 spec 內容需依 workflow 逐份建立並走 approvals。

### 1.2 Spec 之間的依賴順序

- 先做 spec A（可跑起來、依賴與配置就位）
- 再做 spec B（架構邊界可被測試守門）
- 最後做 spec C（商品閉環：Domain → UseCase → REST API）

## 2. Spec A：platform-bootstrap-level0

### 2.1 目的

把專案以「可運行 + 可測試」的最小骨架拉起來，提供後續所有 spec 的落地基礎。

### 2.2 必要輸出文檔

- `.spec-workflow/specs/platform-bootstrap-level0/requirements.md`
- `.spec-workflow/specs/platform-bootstrap-level0/design.md`
- `.spec-workflow/specs/platform-bootstrap-level0/tasks.md`

### 2.3 實作任務清單（建議 tasks.md 粒度）

- 任務 A1：建立 Gradle 專案骨架與版本鎖定
  - 產出/修改：`build.gradle.kts`, `settings.gradle.kts`, `gradle.properties`, `gradlew*`
  - 依賴必含：web、data-jpa、h2、actuator、test、archunit-junit5
  - 驗收：`./gradlew test` 可執行（可先只有空測試）

- 任務 A2：建立 Spring Boot 啟動類與基本啟動測試
  - 新增：`src/main/java/com/loombook/LoombookApplication.java`
  - 新增：最小的 `@SpringBootTest` 啟動測試（確保 context 可起）

- 任務 A3：建立 `application.yml` 並啟用 Virtual Threads
  - 新增：`src/main/resources/application.yml`
  - 必含：`server.port: 8080`、`spring.threads.virtual.enabled: true`

- 任務 A4：配置 H2 datasource + JPA 最小配置
  - 在 `application.yml` 加入 H2 + JPA 最小設定（允許 `ddl-auto: create-drop`）
  - 驗收：啟動時不因 datasource/JPA 報錯

- 任務 A5：啟用 Actuator 並驗證 health endpoint
  - 確保 `spring-boot-starter-actuator` 生效
  - 驗收：`GET /actuator/health` 回 200 且狀態 UP

- 任務 A6：建立最小「運行指令」驗收腳本/說明（可選）
  - 不要求額外工具，只需確保 DoD 中的兩條命令跑通：`./gradlew test`、`./gradlew bootRun`

## 3. Spec B：architecture-guardrails-level0

### 3.1 目的

把六角架構的「包結構 + 依賴方向」具體化，並用 ArchUnit 形成可自動驗收的守門規則，防止後續開發偏離。

### 3.2 必要輸出文檔

- `.spec-workflow/specs/architecture-guardrails-level0/requirements.md`
- `.spec-workflow/specs/architecture-guardrails-level0/design.md`
- `.spec-workflow/specs/architecture-guardrails-level0/tasks.md`

### 3.3 實作任務清單（建議 tasks.md 粒度）

- 任務 B1：建立六角架構 package 結構（空包/空類即可）
  - 必須存在 package（依 steering/FRS）：
    - `com.loombook.domain.model`
    - `com.loombook.domain.port`
    - `com.loombook.application.command`
    - `com.loombook.application.query`
    - `com.loombook.infrastructure.persistence.jpa.entity`
    - `com.loombook.infrastructure.persistence.jpa.repository`
    - `com.loombook.infrastructure.persistence.jpa.adapter`
    - `com.loombook.api.rest`

- 任務 B2：新增 ArchUnit 測試（最小兩條規則）
  - 新增：`src/test/java/.../ArchitectureTest.java`
  - 規則至少包含：
    - Rule A：`com.loombook.domain..` 不得依賴 `org.springframework..`
    - Rule B：`com.loombook.api..` 不得依賴 `com.loombook.infrastructure..`
  - 驗收：`./gradlew test` 通過

- 任務 B3：定義「依賴方向」的團隊約束（測試命名/套件命名）
  - 不新增框架與工具
  - 目標：讓後續 spec C 的任務可以在固定路徑中落地，避免命名漂移

## 4. Spec C：product-catalog-level0

### 4.1 目的

完成 Level 0 最小商品閉環：

- `POST /api/products` 建立商品（201/400）
- `GET /api/products/{id}` 查詢商品（200/404）
- 商品必須真實落庫（H2 + JPA），且必須遵守六角邊界（Controller → UseCase → Domain Port；JPA 在 adapter 內部）。

### 4.2 必要輸出文檔

- `.spec-workflow/specs/product-catalog-level0/requirements.md`
- `.spec-workflow/specs/product-catalog-level0/design.md`
- `.spec-workflow/specs/product-catalog-level0/tasks.md`

### 4.3 實作任務清單（建議 tasks.md 粒度，含 TDD 順序）

> 下列任務刻意按「測試先行」拆分；每個任務都應在 tasks.md 中標註對應的需求段落（例如 FRS 第 6/8/9 節）。

#### 4.3.1 Domain（tests → code）

- 任務 C1：Domain 測試—Value Objects 不變性
  - 新增測試：`ProductId` / `ProductName` / `Price` 的非法輸入（null/空白/負數）應拋出例外
  - 驗收：測試紅燈（尚未實作）

- 任務 C2：Domain 實作—Value Objects
  - 新增：
    - `com.loombook.domain.model.ProductId`
    - `com.loombook.domain.model.ProductName`
    - `com.loombook.domain.model.Price`
  - 建議：用 `record + compact constructor` 表達不變性
  - 驗收：C1 綠燈

- 任務 C3：Domain 測試—Product 聚合建立與狀態
  - 新增測試：Product 建立時必須持有 `id/name/priceCents`，且 VO 約束生效

- 任務 C4：Domain 實作—Product 聚合
  - 新增：`com.loombook.domain.model.Product`

- 任務 C5：Domain Port 測試—ProductRepository 合約（可用假實作/介面測試策略）
  - 目標：明確 `save`/`findById` 的行為期望（例如 Optional）

- 任務 C6：Domain Port 實作—ProductRepository
  - 新增：`com.loombook.domain.port.ProductRepository`

#### 4.3.2 Application UseCases（tests → code）

- 任務 C7：UseCase 測試—CreateProduct
  - 測試內容：
    - 輸入合法時可建立並回傳 `id/name/priceCents`
    - 輸入不合法（name 空白、priceCents < 0）應失敗
  - 約束：UseCase 只能依賴 `ProductRepository`（Domain Port）

- 任務 C8：UseCase 實作—CreateProductUseCase
  - 新增：`com.loombook.application.command.CreateProductUseCase`
  - 需要：輸入 DTO（可用 record）與輸出 DTO

- 任務 C9：UseCase 測試—GetProductById
  - 測試內容：
    - 存在時回傳商品
    - 不存在時回傳 not found 的結果（建議以 Optional/自訂例外二擇一，但需與 API 404 對齊）

- 任務 C10：UseCase 實作—GetProductByIdUseCase
  - 新增：`com.loombook.application.query.GetProductByIdUseCase`

#### 4.3.3 Infrastructure JPA（tests → code / 或以 SpringBootTest 驗證）

- 任務 C11：Persistence 測試—JPA Adapter 可落庫
  - 驗證：透過 Adapter + H2 實際寫入、查回
  - 禁止：Controller 或 UseCase 直接注入 `JpaRepository`

- 任務 C12：JPA Entity / Spring Data Repository
  - 新增：
    - `com.loombook.infrastructure.persistence.jpa.entity.ProductEntity`
    - `com.loombook.infrastructure.persistence.jpa.repository.SpringDataProductJpaRepository`

- 任務 C13：JPA Adapter 實作—JpaProductRepositoryAdapter
  - 新增：`com.loombook.infrastructure.persistence.jpa.adapter.JpaProductRepositoryAdapter`
  - 實作：`ProductRepository`
  - 責任：Domain ↔ Entity 的映射（保持 Domain 不依賴 JPA）

#### 4.3.4 API（tests → code）

- 任務 C14：API 測試—POST /api/products
  - 驗證：
    - 201：回傳 `id/name/priceCents`
    - 400：name 空白或 priceCents < 0
  - 建議：使用 MockMvc（Spring MVC）

- 任務 C15：API 實作—ProductController 的建立商品
  - 新增：`com.loombook.api.rest.ProductController`
  - 新增 DTO：request/response 使用 `record`
  - 狀態碼必須符合 201/400

- 任務 C16：API 測試—GET /api/products/{id}
  - 驗證：
    - 200：存在時回傳 `id/name/priceCents`
    - 404：不存在時回 404

- 任務 C17：API 實作—ProductController 的查詢商品
  - `GET /api/products/{id}`
  - 狀態碼必須符合 200/404

#### 4.3.5 交付整合驗收

- 任務 C18：整體驗收—DoD 命令與 ArchUnit 維持綠燈
  - `./gradlew test` 全綠
  - `./gradlew bootRun` 啟動成功
  - `GET /actuator/health` UP
  - 再次確認：API/UseCase/Infra 依賴方向未被破壞（ArchUnit 維持通過）

## 5. 建議的 Spec 建立與審核節奏（避免返工）

- 每個 spec 依 workflow 逐步建立：`requirements.md` → 審核 → `design.md` → 審核 → `tasks.md` → 審核
- `tasks.md` 的任務粒度以「1–3 個檔案/任務」為上限，方便 TDD 迭代與 log-implementation 記錄
- 實作階段每完成一個 task：
  - 先在 `tasks.md` 把該任務標記為 `[-]`
  - 完成後呼叫 `log-implementation`
  - 再把任務標記為 `[x]`

---

## 本文件狀態

- 本分析文件已覆蓋 Level 0 必要 spec 與任務清單
- 後續可以依此逐一建立 spec 文檔並進入正式 workflow（含 approvals）
