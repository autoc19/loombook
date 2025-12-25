# Structure Steering

## 1. 架構風格

本專案採用六角架構（Hexagonal Architecture / Ports & Adapters），並結合 DDD 的最小集合以確保：

- Domain 模型與規則可獨立演進
- Application 用例可被測試與重用
- Infrastructure 細節（JPA/H2 等）不污染核心
- API 層保持薄、只負責 HTTP/DTO/狀態碼

## 2. 分層與依賴方向（硬規則）

- Domain：不得依賴 Spring/JPA/Web
- Application：只依賴 Domain（含 Domain Port）
- API：只依賴 Application，不得依賴 Infrastructure
- Infrastructure：可依賴 Spring/JPA，並以 Adapter 形式實作 Domain Port

依賴方向示意：

- `api -> application -> domain`
- `infrastructure -> domain`

## 3. Package 結構（必須存在）

以下 package 必須存在：

- `com.loombook.domain.model`
- `com.loombook.domain.port`
- `com.loombook.application.command`
- `com.loombook.application.query`
- `com.loombook.infrastructure.persistence.jpa.entity`
- `com.loombook.infrastructure.persistence.jpa.repository`
- `com.loombook.infrastructure.persistence.jpa.adapter`
- `com.loombook.api.rest`

## 4. 核心模組职责

### 4.1 Domain

- `Product`：Aggregate Root
- Value Objects：
  - `ProductId(String value)`：不可為 null/空白
  - `ProductName(String value)`：不可為 null/空白
  - `Price(int priceCents)`：必須 >= 0
- Port：`ProductRepository`（介面定義於 `domain.port`）

### 4.2 Application

- Command UseCase：`CreateProductUseCase`
- Query UseCase：`GetProductByIdUseCase`

硬規則：

- Controller 只能呼叫 UseCase
- UseCase 只能依賴 Domain Port（不得注入 `JpaRepository`）

### 4.3 Infrastructure（JPA 持久化）

Repository Adapter 三段式（強制）：

1. Domain Port：`domain.port.ProductRepository`
2. Spring Data Repository（內部元件）：`infrastructure.persistence.jpa.repository.SpringDataProductJpaRepository`
3. Adapter：`infrastructure.persistence.jpa.adapter.JpaProductRepositoryAdapter implements ProductRepository`

限制：

- JPA Repository 只能被 Adapter 使用
- 不允許 Controller 或 UseCase 直接注入 `SpringDataProductJpaRepository`

### 4.4 API（REST）

- `api.rest.ProductController` 提供：
  - `POST /api/products`
  - `GET /api/products/{id}`

DTO 约定：

- Request/Response DTO 使用 `record`
- HTTP 狀態碼需與 Level 0 FRS 完全一致（201/400/200/404）

## 5. 測試結構與守門

- Domain tests：VO 不變性
- UseCase tests：CreateProduct / GetProductById 行為
- API tests：201/400/200/404
- `ArchitectureTest`：最少兩條 ArchUnit 規則（Domain 無 Spring 依賴；API 不依賴 Infrastructure）

## 6. 可觀測性

- 必須啟用 Actuator
- 必須可訪問 `GET /actuator/health` 且為 UP
