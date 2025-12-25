# Tech Steering

## 1. 技術棧（Level 0 鎖定）

- Java：21 (LTS)
- Spring Boot：3.4.13（需以 `gradle.properties` 鎖定版本）
- Web：Spring MVC（同步）
- 并發模型：Java 21 Virtual Threads（必須啟用 `spring.threads.virtual.enabled: true`）
- DB：H2 in-memory（真實落庫）
- ORM：Spring Data JPA
- 健康檢查：Spring Boot Actuator（至少提供 `/actuator/health`）
- 測試：spring-boot-starter-test
- 架構守門：ArchUnit（JUnit5）

## 2. 关键技術決策與硬性約束

### 2.1 并發主線（單一模型）

- 保持同步 Spring MVC 風格
- 必須啟用 Virtual Threads
- **禁止** WebFlux/Reactor 混搭（避免多并發模型帶來複雜度）

### 2.2 持久化策略（真實形態）

- 必須使用 H2 + JPA 完成「可落庫」的最小閉環
- 禁止用 `Map` 代替資料庫
- Level 0 允許 `ddl-auto: create-drop` 作為最小配置

### 2.3 六角架構 + DDD 最小集合

- Domain 層不可依賴 Spring/JPA/Web
- Repository 必須使用 Port + Adapter：
  - Domain 定義 `ProductRepository`（Port）
  - Infrastructure 使用 `JpaRepository`（內部）
  - Adapter（`JpaProductRepositoryAdapter`）實作 Port
- Controller 只能呼叫 UseCase；UseCase 只能依賴 Domain Port

### 2.4 測試與品質門禁（強制 TDD）

- 必須遵循 Red → Green → Refactor
- 禁止先寫功能再補測試
- 固定開發順序：
  - Domain tests → Domain code → UseCase tests → UseCase code → API tests → API code

### 2.5 ArchUnit 最小規則（Level 0）

必須至少包含兩條核心規則：

- Rule A：`com.loombook.domain..` 不得依賴 `org.springframework..`
- Rule B：`com.loombook.api..` 不得依賴 `com.loombook.infrastructure..`

## 3. 依賴與配置要求

### 3.1 必要依賴（Gradle）

必須包含：

- `spring-boot-starter-web`
- `spring-boot-starter-data-jpa`
- `com.h2database:h2`
- `spring-boot-starter-actuator`
- `spring-boot-starter-test`
- `com.tngtech.archunit:archunit-junit5`（或等價）

### 3.2 必要配置（application.yml）

- `server.port: 8080`
- `spring.threads.virtual.enabled: true`
- H2 datasource + JPA 最小配置（可使用 `create-drop`）
- Actuator health 使用預設即可（確保 `/actuator/health` 可訪問且 UP）

## 4. 明確禁止（避免發散）

- Spring Security / OAuth2 / RBAC / 多租戶 TenantId
- Kafka / Saga / 支付/庫存扣減/訂單能力
- Structured Concurrency / Scoped Values
- Testcontainers / E2E
- Tracing / Metrics / Log 規範（Level 0 僅 health）
- OpenAPI 檔案

## 5. Level 0 → Level 1+ 演進約束

- DB 切換（H2 → Postgres）允許修改：
  - datasource 配置、依賴、遷移工具（Liquibase/Flyway）
- DB 切換禁止修改：
  - Domain 模型、Domain Port、UseCase 對 Port 的依賴方式
- 測試演進：
  - Level 0：H2 + SpringBootTest
  - Level 1：可引入 Testcontainers Postgres（替換/新增集成測試），但保持 Domain 單元測試不變
