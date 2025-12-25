# Design Document - Product Catalog Level 0

## Overview

本設計文檔描述如何實現 Level 0 的商品最小閉環。採用六角架構與 DDD 最小集合，嚴格遵循 TDD 開發順序，確保 Domain 層框架無關，API 層與 Infrastructure 層隔離。

## Steering Document Alignment

### Technical Standards (tech.md)
- 六角架構 + DDD 最小集合
- Repository Port + Adapter 模式
- 強制 TDD：Domain tests → Domain code → UseCase tests → UseCase code → API tests → API code
- H2 + Spring Data JPA

### Project Structure (structure.md)
- Domain：domain.model（Product, Value Objects）、domain.port（ProductRepository）
- Application：application.command（CreateProductUseCase）、application.query（GetProductByIdUseCase）
- Infrastructure：infrastructure.persistence.jpa.*（Entity, Repository, Adapter）
- API：api.rest（ProductController）

## Code Reuse Analysis

### Existing Components to Leverage
- **platform-bootstrap-level0**: H2 + JPA 配置已就位
- **architecture-guardrails-level0**: Package 結構與 ArchUnit 規則已就位

### Integration Points
- Spring Data JPA（透過 Adapter）
- Spring MVC（REST Controller）
- H2 Database

## Architecture

```
┌─────────────────────────────────────────────────────────────────┐
│                         API Layer                               │
│  ┌─────────────────────────────────────────────────────────┐   │
│  │ ProductController                                        │   │
│  │   POST /api/products → CreateProductUseCase             │   │
│  │   GET /api/products/{id} → GetProductByIdUseCase        │   │
│  └─────────────────────────────────────────────────────────┘   │
│                              │                                  │
│                              ▼                                  │
│                      Application Layer                          │
│  ┌─────────────────────────────────────────────────────────┐   │
│  │ CreateProductUseCase          GetProductByIdUseCase     │   │
│  │   depends on ProductRepository (Port)                   │   │
│  └─────────────────────────────────────────────────────────┘   │
│                              │                                  │
│                              ▼                                  │
│                        Domain Layer                             │
│  ┌─────────────────────────────────────────────────────────┐   │
│  │ Product (Aggregate Root)                                │   │
│  │ ProductId, ProductName, Price (Value Objects)           │   │
│  │ ProductRepository (Port Interface)                      │   │
│  └─────────────────────────────────────────────────────────┘   │
│                              ▲                                  │
│                              │                                  │
│                    Infrastructure Layer                         │
│  ┌─────────────────────────────────────────────────────────┐   │
│  │ JpaProductRepositoryAdapter implements ProductRepository│   │
│  │   uses SpringDataProductJpaRepository                   │   │
│  │ ProductEntity (JPA Entity)                              │   │
│  └─────────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────────┘
```

## Components and Interfaces

### Domain Layer

#### Value Objects
- **ProductId**
  - Purpose: 商品唯一識別碼
  - Implementation: `record ProductId(String value)` + compact constructor 驗證
  - Validation: value 不得為 null 或空白

- **ProductName**
  - Purpose: 商品名稱
  - Implementation: `record ProductName(String value)` + compact constructor 驗證
  - Validation: value 不得為 null 或空白

- **Price**
  - Purpose: 商品價格（以分為單位）
  - Implementation: `record Price(int priceCents)` + compact constructor 驗證
  - Validation: priceCents 必須 >= 0

#### Aggregate Root
- **Product**
  - Purpose: 商品聚合根
  - Fields: ProductId id, ProductName name, Price price
  - Implementation: class 或 record，封裝 Value Objects

#### Port
- **ProductRepository**
  - Purpose: 定義持久化契約
  - Methods:
    - `Product save(Product product)`
    - `Optional<Product> findById(ProductId id)`

### Application Layer

#### Command UseCase
- **CreateProductUseCase**
  - Purpose: 建立商品用例
  - Input: CreateProductCommand (name, priceCents)
  - Output: CreateProductResult (id, name, priceCents)
  - Dependencies: ProductRepository (Port)

#### Query UseCase
- **GetProductByIdUseCase**
  - Purpose: 查詢商品用例
  - Input: ProductId
  - Output: Optional<ProductResult> 或拋出 NotFoundException
  - Dependencies: ProductRepository (Port)

### Infrastructure Layer

#### JPA Entity
- **ProductEntity**
  - Purpose: JPA 持久化實體
  - Fields: String id, String name, int priceCents
  - Annotations: @Entity, @Id

#### Spring Data Repository
- **SpringDataProductJpaRepository**
  - Purpose: Spring Data JPA 介面
  - Extends: JpaRepository<ProductEntity, String>

#### Adapter
- **JpaProductRepositoryAdapter**
  - Purpose: 實作 Domain Port，橋接 Domain 與 JPA
  - Implements: ProductRepository
  - Dependencies: SpringDataProductJpaRepository
  - Responsibility: Domain ↔ Entity 映射

### API Layer

#### Controller
- **ProductController**
  - Purpose: REST 端點
  - Endpoints:
    - POST /api/products → 201/400
    - GET /api/products/{id} → 200/404
  - Dependencies: CreateProductUseCase, GetProductByIdUseCase

#### DTOs
- **CreateProductRequest**: record (name, priceCents)
- **ProductResponse**: record (id, name, priceCents)

## Data Models

### Domain Model
```java
record ProductId(String value) {
    public ProductId {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("ProductId cannot be null or blank");
        }
    }
}

record ProductName(String value) {
    public ProductName {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("ProductName cannot be null or blank");
        }
    }
}

record Price(int priceCents) {
    public Price {
        if (priceCents < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
    }
}
```

### JPA Entity
```java
@Entity
@Table(name = "products")
public class ProductEntity {
    @Id
    private String id;
    private String name;
    private int priceCents;
    // getters, setters, constructors
}
```

## Error Handling

### Error Scenarios
1. **Value Object 驗證失敗**
   - Handling: 拋出 IllegalArgumentException
   - User Impact: API 回傳 400

2. **商品不存在**
   - Handling: UseCase 回傳 Optional.empty() 或拋出 NotFoundException
   - User Impact: API 回傳 404

3. **資料庫錯誤**
   - Handling: Spring 預設例外處理
   - User Impact: API 回傳 500

## Testing Strategy

### Unit Testing (TDD - Red → Green → Refactor)

#### Domain Tests（先寫）
- Value Objects 不變性測試
- Product 聚合建立測試
- ProductRepository 合約測試（可用假實作）

#### UseCase Tests（Domain 完成後）
- CreateProductUseCase 行為測試
- GetProductByIdUseCase 行為測試
- 使用 Mock ProductRepository

### Integration Testing

#### API Tests（UseCase 完成後）
- POST /api/products 201/400 測試
- GET /api/products/{id} 200/404 測試
- 使用 MockMvc + SpringBootTest

#### Persistence Tests
- JpaProductRepositoryAdapter 落庫測試
- 使用 H2 + SpringBootTest

### End-to-End Testing
- Level 0 明確禁止 E2E 測試
