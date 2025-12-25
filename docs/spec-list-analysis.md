# Spec 規劃清單分析

> 基於 steering 文檔與 Level 0 FRS 分析，完成本專案所需的 spec 數量與清單。

## 結論：需要 3 個 Spec

根據 Level 0 的範圍約束、依賴順序與可驗收性，本專案需要 **3 個 spec** 來完成最小閉環。

---

## Spec 清單

| # | Spec 名稱 | 目的 | 依賴 |
|---|-----------|------|------|
| 1 | `platform-bootstrap-level0` | 專案骨架、依賴配置、啟動驗證 | 無 |
| 2 | `architecture-guardrails-level0` | 六角架構包結構、ArchUnit 守門規則 | Spec 1 |
| 3 | `product-catalog-level0` | 商品閉環（Domain → UseCase → API） | Spec 1, 2 |

---

## Spec 詳細說明

### Spec 1: `platform-bootstrap-level0`

**目的**：建立可運行、可測試的專案骨架

**交付內容**：
- Gradle 專案配置（build.gradle.kts, settings.gradle.kts, gradle.properties）
- Spring Boot 啟動類（LoombookApplication.java）
- application.yml（Virtual Threads、H2、JPA、Actuator）
- 啟動測試與 health endpoint 驗證

**驗收標準**：
- `./gradlew test` 可執行
- `./gradlew bootRun` 可啟動
- `GET /actuator/health` 回傳 200 UP

---

### Spec 2: `architecture-guardrails-level0`

**目的**：建立六角架構邊界與自動化守門

**交付內容**：
- 六角架構 package 結構（8 個必要 package）
- ArchUnit 測試（最少 2 條規則）
  - Rule A：Domain 不依賴 Spring
  - Rule B：API 不依賴 Infrastructure

**驗收標準**：
- 所有必要 package 存在
- `./gradlew test` 通過（含 ArchUnit）

---

### Spec 3: `product-catalog-level0`

**目的**：完成 Level 0 商品最小閉環

**交付內容**：

**Domain 層**：
- Value Objects：ProductId, ProductName, Price
- Aggregate Root：Product
- Port：ProductRepository

**Application 層**：
- CreateProductUseCase
- GetProductByIdUseCase

**Infrastructure 層**：
- ProductEntity
- SpringDataProductJpaRepository
- JpaProductRepositoryAdapter

**API 層**：
- ProductController
- Request/Response DTOs

**測試**：
- Domain tests（VO 不變性）
- UseCase tests（CreateProduct, GetProductById）
- API tests（201/400/200/404）

**驗收標準**：
- `POST /api/products`：201/400
- `GET /api/products/{id}`：200/404
- 強制 TDD 順序：Domain tests → Domain code → UseCase tests → UseCase code → API tests → API code
- ArchUnit 規則維持通過

---

## 執行順序

```
Spec 1 (platform-bootstrap-level0)
    ↓
Spec 2 (architecture-guardrails-level0)
    ↓
Spec 3 (product-catalog-level0)
```

---

## 任務數量估算

| Spec | 預估任務數 |
|------|-----------|
| platform-bootstrap-level0 | 5-6 |
| architecture-guardrails-level0 | 3 |
| product-catalog-level0 | 17-18 |
| **總計** | **25-27** |

---

## 備註

- 每個 spec 需建立三份文檔：requirements.md、design.md、tasks.md
- 實作階段需遵循 TDD（Red → Green → Refactor）
- 每完成一個 task 需呼叫 `log-implementation` 記錄
