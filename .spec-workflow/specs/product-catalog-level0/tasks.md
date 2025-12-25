# Tasks Document - Product Catalog Level 0

> TDD 強制順序：Domain tests → Domain code → UseCase tests → UseCase code → API tests → API code

## Domain Layer (Tests → Code)

- [x] 1. Domain 測試 - Value Objects 不變性
- [x] 2. Domain 實作 - Value Objects
- [x] 3. Domain 測試 - Product 聚合建立與狀態
- [x] 4. Domain 實作 - Product 聚合
- [x] 5. Domain Port - ProductRepository 介面

## Application Layer (Tests → Code)

- [x] 6. UseCase 測試 - CreateProduct
- [x] 7. UseCase 實作 - CreateProductUseCase
- [x] 8. UseCase 測試 - GetProductById
- [x] 9. UseCase 實作 - GetProductByIdUseCase

## Infrastructure Layer (Tests → Code)

- [x] 10. Persistence 測試 - JPA Adapter 可落庫
- [x] 11. JPA Entity 與 Spring Data Repository
- [x] 12. JPA Adapter 實作 - JpaProductRepositoryAdapter

## API Layer (Tests → Code)

- [x] 13. API 測試 - POST /api/products
- [x] 14. API 實作 - ProductController POST
- [x] 15. API 測試 - GET /api/products/{id}
- [x] 16. API 實作 - ProductController GET

## 交付整合驗收

- [x] 17. 整體驗收 - DoD 命令與 ArchUnit 維持綠燈
