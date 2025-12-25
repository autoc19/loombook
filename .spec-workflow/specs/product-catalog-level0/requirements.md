# Requirements Document - Product Catalog Level 0

## Introduction

本 spec 負責實現 Level 0 的商品最小閉環：商品建立與查詢。這是 Level 0 的核心業務 spec，依賴於 platform-bootstrap-level0 與 architecture-guardrails-level0 完成。必須嚴格遵循 TDD（Red → Green → Refactor）與六角架構邊界。

## Alignment with Product Vision

根據 product.md，Level 0 必須提供的最小業務閉環：
- 商品建立（POST /api/products）
- 依商品 ID 查詢（GET /api/products/{id}）
- 商品必須真實落庫（H2 + JPA）

本 spec 直接實現這些目標，並確保：
- 強制 TDD 的交付紀律
- 六角架構邊界不被破壞
- Domain 層完全框架無關

## Requirements

### Requirement 1: Domain Value Objects

**User Story:** 作為開發者，我需要不可變的 Value Objects，以便確保領域模型的完整性。

#### Acceptance Criteria

1. WHEN 建立 ProductId 時傳入 null 或空白 THEN 系統 SHALL 拋出例外
2. WHEN 建立 ProductName 時傳入 null 或空白 THEN 系統 SHALL 拋出例外
3. WHEN 建立 Price 時傳入負數 THEN 系統 SHALL 拋出例外
4. WHEN Value Object 建立成功 THEN 系統 SHALL 保證不可變性

### Requirement 2: Domain Aggregate - Product

**User Story:** 作為開發者，我需要 Product 聚合根，以便封裝商品的業務規則。

#### Acceptance Criteria

1. WHEN 建立 Product THEN 系統 SHALL 持有 id, name, priceCents
2. WHEN Product 建立時 THEN 系統 SHALL 驗證所有 Value Object 約束
3. IF Product 的任何 Value Object 無效 THEN 系統 SHALL 拒絕建立

### Requirement 3: Domain Port - ProductRepository

**User Story:** 作為開發者，我需要 Repository 介面定義在 Domain 層，以便 Domain 不依賴具體實作。

#### Acceptance Criteria

1. WHEN 定義 ProductRepository THEN 系統 SHALL 放在 domain.port 包下
2. WHEN ProductRepository 定義 save 方法 THEN 系統 SHALL 接受 Product 並回傳 Product
3. WHEN ProductRepository 定義 findById 方法 THEN 系統 SHALL 回傳 Optional<Product>

### Requirement 4: Application UseCase - CreateProduct

**User Story:** 作為 API 呼叫端，我需要建立商品的用例，以便新增商品到系統。

#### Acceptance Criteria

1. WHEN 輸入合法（name 非空、priceCents >= 0）THEN 系統 SHALL 建立商品並回傳 id, name, priceCents
2. WHEN 輸入不合法（name 空白）THEN 系統 SHALL 回傳錯誤
3. WHEN 輸入不合法（priceCents < 0）THEN 系統 SHALL 回傳錯誤
4. WHEN UseCase 執行 THEN 系統 SHALL 只依賴 ProductRepository（Domain Port）

### Requirement 5: Application UseCase - GetProductById

**User Story:** 作為 API 呼叫端，我需要查詢商品的用例，以便取得商品資訊。

#### Acceptance Criteria

1. WHEN 商品存在 THEN 系統 SHALL 回傳商品資訊（id, name, priceCents）
2. WHEN 商品不存在 THEN 系統 SHALL 回傳 not found 結果
3. WHEN UseCase 執行 THEN 系統 SHALL 只依賴 ProductRepository（Domain Port）

### Requirement 6: Infrastructure - JPA Persistence

**User Story:** 作為開發者，我需要 JPA 實作持久化，以便商品真實落庫。

#### Acceptance Criteria

1. WHEN 透過 Adapter 儲存商品 THEN 系統 SHALL 寫入 H2 資料庫
2. WHEN 透過 Adapter 查詢商品 THEN 系統 SHALL 從 H2 資料庫讀取
3. WHEN JPA Repository 被使用 THEN 系統 SHALL 只能被 Adapter 注入（不得被 Controller 或 UseCase 直接注入）

### Requirement 7: API - POST /api/products

**User Story:** 作為 API 呼叫端，我需要建立商品的 REST 端點。

#### Acceptance Criteria

1. WHEN POST /api/products 且輸入合法 THEN 系統 SHALL 回傳 HTTP 201 與 {id, name, priceCents}
2. WHEN POST /api/products 且 name 空白 THEN 系統 SHALL 回傳 HTTP 400
3. WHEN POST /api/products 且 priceCents < 0 THEN 系統 SHALL 回傳 HTTP 400
4. WHEN Controller 處理請求 THEN 系統 SHALL 只呼叫 UseCase（不得直接呼叫 Repository）

### Requirement 8: API - GET /api/products/{id}

**User Story:** 作為 API 呼叫端，我需要查詢商品的 REST 端點。

#### Acceptance Criteria

1. WHEN GET /api/products/{id} 且商品存在 THEN 系統 SHALL 回傳 HTTP 200 與 {id, name, priceCents}
2. WHEN GET /api/products/{id} 且商品不存在 THEN 系統 SHALL 回傳 HTTP 404
3. WHEN Controller 處理請求 THEN 系統 SHALL 只呼叫 UseCase（不得直接呼叫 Repository）

## Non-Functional Requirements

### Code Architecture and Modularity
- **Single Responsibility Principle**: 每個類別有明確單一職責
- **Modular Design**: 嚴格遵循六角架構分層
- **Dependency Management**: Controller → UseCase → Domain Port；Adapter 實作 Domain Port
- **Clear Interfaces**: Domain Port 定義清晰的 Repository 契約

### Performance
- Level 0 不做效能指標驗收

### Security
- Level 0 不實作安全機制（明確禁止 Spring Security）

### Reliability
- 所有測試必須穩定通過
- ArchUnit 規則必須維持綠燈

### Usability
- API 回應格式清晰一致
- 錯誤訊息有意義

### TDD Compliance
- 必須遵循 Red → Green → Refactor
- 固定開發順序：Domain tests → Domain code → UseCase tests → UseCase code → API tests → API code
