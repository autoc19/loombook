# 03 - product-catalog-level0 任務總結

## 概述

本文檔記錄 `product-catalog-level0` spec 的完整實現過程，包括任務執行順序、遇到的問題、解決方案及最終驗收結果。

**執行時間**：2025-12-25
**總任務數**：17 個
**完成狀態**：✅ 全部完成

---

## 任務執行詳細記錄

### Domain Layer (任務 1-5)

#### 任務 1-2：Value Objects（測試 + 實作）

**目標**：
- 建立 ProductId、ProductName、Price 三個 Value Objects
- 使用 record + compact constructor 表達不變性

**執行過程**：

1. 建立 `ValueObjectsTest.java` 測試：
   - ProductId：null/空白值應拋出 IllegalArgumentException
   - ProductName：null/空白值應拋出 IllegalArgumentException
   - Price：負數應拋出 IllegalArgumentException

2. 實作三個 Value Objects：
   ```java
   public record ProductId(String value) {
       public ProductId {
           if (value == null || value.isBlank()) {
               throw new IllegalArgumentException("ProductId cannot be null or blank");
           }
       }
   }
   ```

**遇到問題**：無

**產出檔案**：
- `src/test/java/com/loombook/domain/model/ValueObjectsTest.java`
- `src/main/java/com/loombook/domain/model/ProductId.java`
- `src/main/java/com/loombook/domain/model/ProductName.java`
- `src/main/java/com/loombook/domain/model/Price.java`

---

#### 任務 3-4：Product 聚合（測試 + 實作）

**目標**：
- 建立 Product 聚合根
- 封裝 Value Objects

**執行過程**：

1. 建立 `ProductTest.java` 測試：
   - 建立有效的 Product
   - 驗證必須持有所有必要屬性
   - 透過 Product 建立時 Value Object 約束應生效

2. 實作 Product 聚合根：
   ```java
   public record Product(ProductId id, ProductName name, Price price) {
       public Product {
           if (id == null) throw new IllegalArgumentException("Product id cannot be null");
           if (name == null) throw new IllegalArgumentException("Product name cannot be null");
           if (price == null) throw new IllegalArgumentException("Product price cannot be null");
       }
   }
   ```

**遇到問題**：無

**產出檔案**：
- `src/test/java/com/loombook/domain/model/ProductTest.java`
- `src/main/java/com/loombook/domain/model/Product.java`

---

#### 任務 5：ProductRepository 介面

**目標**：
- 定義 Domain Port 介面
- 方法簽名使用 Domain 類型

**執行過程**：

1. 建立 `ProductRepository.java` 介面：
   ```java
   public interface ProductRepository {
       Product save(Product product);
       Optional<Product> findById(ProductId id);
   }
   ```

**遇到問題**：無

**產出檔案**：
- `src/main/java/com/loombook/domain/port/ProductRepository.java`

---

### Application Layer (任務 6-9)

#### 任務 6-7：CreateProductUseCase（測試 + 實作）

**目標**：
- 建立商品用例
- 使用 Mock ProductRepository 測試

**執行過程**：

1. 建立測試：
   - 輸入合法時應建立商品並回傳結果
   - name 空白時應拋出例外
   - priceCents 為負數時應拋出例外

2. 實作 UseCase 與 DTOs：
   - `CreateProductCommand`：輸入 DTO
   - `CreateProductResult`：輸出 DTO
   - `CreateProductUseCase`：用例實作

**遇到問題**：無

**產出檔案**：
- `src/test/java/com/loombook/application/command/CreateProductUseCaseTest.java`
- `src/main/java/com/loombook/application/command/CreateProductCommand.java`
- `src/main/java/com/loombook/application/command/CreateProductResult.java`
- `src/main/java/com/loombook/application/command/CreateProductUseCase.java`

---

#### 任務 8-9：GetProductByIdUseCase（測試 + 實作）

**目標**：
- 查詢商品用例
- 商品不存在時回傳 Optional.empty()

**執行過程**：

1. 建立測試：
   - 商品存在時應回傳商品資訊
   - 商品不存在時應回傳空

2. 實作 UseCase：
   - `ProductResult`：輸出 DTO
   - `GetProductByIdUseCase`：用例實作

**遇到問題**：無

**產出檔案**：
- `src/test/java/com/loombook/application/query/GetProductByIdUseCaseTest.java`
- `src/main/java/com/loombook/application/query/ProductResult.java`
- `src/main/java/com/loombook/application/query/GetProductByIdUseCase.java`

---

### Infrastructure Layer (任務 10-12)

#### 任務 10-12：JPA 持久化（測試 + 實作）

**目標**：
- 建立 JPA Entity
- 建立 Spring Data Repository
- 建立 Adapter 實作 Domain Port

**執行過程**：

1. 建立 `ProductEntity`：
   ```java
   @Entity
   @Table(name = "products")
   public class ProductEntity {
       @Id
       private String id;
       private String name;
       private int priceCents;
   }
   ```

2. 建立 `SpringDataProductJpaRepository`：
   ```java
   public interface SpringDataProductJpaRepository extends JpaRepository<ProductEntity, String> {}
   ```

3. 建立 `JpaProductRepositoryAdapter`：
   - 實作 `ProductRepository` 介面
   - 負責 Domain ↔ Entity 映射

4. 建立整合測試驗證落庫功能

**遇到問題**：無

**產出檔案**：
- `src/main/java/com/loombook/infrastructure/persistence/jpa/entity/ProductEntity.java`
- `src/main/java/com/loombook/infrastructure/persistence/jpa/repository/SpringDataProductJpaRepository.java`
- `src/main/java/com/loombook/infrastructure/persistence/jpa/adapter/JpaProductRepositoryAdapter.java`
- `src/test/java/com/loombook/infrastructure/persistence/jpa/adapter/JpaProductRepositoryAdapterTest.java`

---

### API Layer (任務 13-16)

#### 任務 13-16：ProductController（測試 + 實作）

**目標**：
- POST /api/products：201/400
- GET /api/products/{id}：200/404

**執行過程**：

1. 建立 Controller 與 DTOs：
   - `CreateProductRequest`：請求 DTO
   - `ProductResponse`：回應 DTO
   - `ProductController`：REST Controller

2. 建立 UseCase 配置：
   - `UseCaseConfig`：Spring Configuration 注入 UseCase

3. 建立 API 測試：
   - POST 201 建立成功
   - POST 400 name 空白
   - POST 400 priceCents 為負數
   - GET 200 商品存在
   - GET 404 商品不存在

**遇到問題**：無

**產出檔案**：
- `src/main/java/com/loombook/api/rest/CreateProductRequest.java`
- `src/main/java/com/loombook/api/rest/ProductResponse.java`
- `src/main/java/com/loombook/api/rest/ProductController.java`
- `src/main/java/com/loombook/config/UseCaseConfig.java`
- `src/test/java/com/loombook/api/rest/ProductControllerTest.java`

---

### 任務 17：整體驗收

**驗收項目**：

1. **./gradlew test**：✅ 全綠
   ```
   BUILD SUCCESSFUL in 9s
   4 actionable tasks: 3 executed, 1 up-to-date
   ```

2. **./gradlew bootRun**：✅ 啟動成功
   ```
   Started LoombookApplication in 2.xxx seconds
   Tomcat started on port 8080
   ```

3. **GET /actuator/health**：✅ UP
   ```json
   {"status":"UP"}
   ```

4. **POST /api/products**：✅ 201
   ```json
   {"id":"d83e1b6c-7f91-4775-b135-2bfccdf904db","name":"Java 程式設計","priceCents":9900}
   ```

5. **GET /api/products/{id}**：✅ 200
   ```json
   {"id":"d83e1b6c-7f91-4775-b135-2bfccdf904db","name":"Java 程式設計","priceCents":9900}
   ```

6. **ArchUnit 規則**：✅ 維持通過

---

## 完成的測試

| 測試類別 | 測試數量 | 結果 |
|---------|---------|------|
| ValueObjectsTest | 9 | ✅ 通過 |
| ProductTest | 3 | ✅ 通過 |
| CreateProductUseCaseTest | 3 | ✅ 通過 |
| GetProductByIdUseCaseTest | 2 | ✅ 通過 |
| JpaProductRepositoryAdapterTest | 2 | ✅ 通過 |
| ProductControllerTest | 5 | ✅ 通過 |
| ArchitectureTest | 2 | ✅ 通過 |
| LoombookApplicationTests | 1 | ✅ 通過 |
| **總計** | **27** | ✅ 全部通過 |

---

## 最終產出檔案清單

### Domain Layer
| 檔案路徑 | 用途 |
|---------|------|
| `src/main/java/com/loombook/domain/model/ProductId.java` | 商品 ID Value Object |
| `src/main/java/com/loombook/domain/model/ProductName.java` | 商品名稱 Value Object |
| `src/main/java/com/loombook/domain/model/Price.java` | 價格 Value Object |
| `src/main/java/com/loombook/domain/model/Product.java` | 商品聚合根 |
| `src/main/java/com/loombook/domain/port/ProductRepository.java` | Repository Port |

### Application Layer
| 檔案路徑 | 用途 |
|---------|------|
| `src/main/java/com/loombook/application/command/CreateProductCommand.java` | 建立商品命令 DTO |
| `src/main/java/com/loombook/application/command/CreateProductResult.java` | 建立商品結果 DTO |
| `src/main/java/com/loombook/application/command/CreateProductUseCase.java` | 建立商品用例 |
| `src/main/java/com/loombook/application/query/ProductResult.java` | 商品查詢結果 DTO |
| `src/main/java/com/loombook/application/query/GetProductByIdUseCase.java` | 查詢商品用例 |

### Infrastructure Layer
| 檔案路徑 | 用途 |
|---------|------|
| `src/main/java/com/loombook/infrastructure/persistence/jpa/entity/ProductEntity.java` | JPA Entity |
| `src/main/java/com/loombook/infrastructure/persistence/jpa/repository/SpringDataProductJpaRepository.java` | Spring Data Repository |
| `src/main/java/com/loombook/infrastructure/persistence/jpa/adapter/JpaProductRepositoryAdapter.java` | Repository Adapter |

### API Layer
| 檔案路徑 | 用途 |
|---------|------|
| `src/main/java/com/loombook/api/rest/CreateProductRequest.java` | 請求 DTO |
| `src/main/java/com/loombook/api/rest/ProductResponse.java` | 回應 DTO |
| `src/main/java/com/loombook/api/rest/ProductController.java` | REST Controller |

### Configuration
| 檔案路徑 | 用途 |
|---------|------|
| `src/main/java/com/loombook/config/UseCaseConfig.java` | UseCase Spring 配置 |

### Tests
| 檔案路徑 | 用途 |
|---------|------|
| `src/test/java/com/loombook/domain/model/ValueObjectsTest.java` | Value Objects 測試 |
| `src/test/java/com/loombook/domain/model/ProductTest.java` | Product 聚合測試 |
| `src/test/java/com/loombook/application/command/CreateProductUseCaseTest.java` | CreateProduct 用例測試 |
| `src/test/java/com/loombook/application/query/GetProductByIdUseCaseTest.java` | GetProductById 用例測試 |
| `src/test/java/com/loombook/infrastructure/persistence/jpa/adapter/JpaProductRepositoryAdapterTest.java` | JPA Adapter 整合測試 |
| `src/test/java/com/loombook/api/rest/ProductControllerTest.java` | API 測試 |

---

## 問題與解決方案總結

本 spec 執行過程中未遇到任何問題，所有任務順利完成。

---

## 結論

`product-catalog-level0` spec 已成功完成，實現了 Level 0 的商品最小閉環。

**驗收結果**：
- ✅ POST /api/products：201/400
- ✅ GET /api/products/{id}：200/404
- ✅ 商品真實落庫（H2 + JPA）
- ✅ 六角架構邊界維持（ArchUnit 通過）
- ✅ TDD 順序遵循（Domain → UseCase → API）
- ✅ Domain 層框架無關
- ✅ 27 個測試全部通過

**Level 0 完成！** 專案已具備：
- 可運行的 Spring Boot 應用程式
- 六角架構 + DDD 最小集合
- 強制 TDD 的測試覆蓋
- ArchUnit 架構守門
- 商品建立與查詢的完整閉環
