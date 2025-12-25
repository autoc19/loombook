# Requirements Document - Platform Bootstrap Level 0

## Introduction

本 spec 負責建立「企業級線上書店與交易平台」的專案骨架，提供可運行、可測試的最小基礎設施。這是 Level 0 的第一個 spec，為後續架構守門與商品閉環提供落地基礎。

## Alignment with Product Vision

根據 product.md，Level 0 的目標是交付「可運行、可測試、可演進的最小系統骨架」。本 spec 直接實現這個目標，確保：
- 專案可以正確編譯與啟動
- Virtual Threads 並發模型就位
- H2 + JPA 持久化基礎就位
- 最小可觀測性（health endpoint）就位

## Requirements

### Requirement 1: Gradle 專案骨架

**User Story:** 作為開發者，我需要一個正確配置的 Gradle 專案，以便能夠編譯、測試與運行應用程式。

#### Acceptance Criteria

1. WHEN 執行 `./gradlew build` THEN 系統 SHALL 成功編譯且無錯誤
2. WHEN 檢查 `gradle.properties` THEN 系統 SHALL 鎖定 Spring Boot 版本為 3.4.13
3. WHEN 檢查依賴 THEN 系統 SHALL 包含以下必要依賴：
   - spring-boot-starter-web
   - spring-boot-starter-data-jpa
   - com.h2database:h2
   - spring-boot-starter-actuator
   - spring-boot-starter-test
   - com.tngtech.archunit:archunit-junit5

### Requirement 2: Spring Boot 啟動類

**User Story:** 作為開發者，我需要一個 Spring Boot 啟動類，以便應用程式能夠正確啟動。

#### Acceptance Criteria

1. WHEN 存在 `LoombookApplication.java` THEN 系統 SHALL 包含 `@SpringBootApplication` 註解
2. WHEN 執行 `@SpringBootTest` THEN 系統 SHALL 成功載入 Spring Context
3. WHEN 執行 `./gradlew bootRun` THEN 系統 SHALL 在 8080 端口啟動監聽

### Requirement 3: Virtual Threads 配置

**User Story:** 作為開發者，我需要啟用 Java 21 Virtual Threads，以便應用程式使用現代並發模型。

#### Acceptance Criteria

1. WHEN 檢查 `application.yml` THEN 系統 SHALL 包含 `spring.threads.virtual.enabled: true`
2. WHEN 應用程式啟動 THEN 系統 SHALL 使用 Virtual Threads 處理請求

### Requirement 4: H2 + JPA 配置

**User Story:** 作為開發者，我需要配置 H2 資料庫與 JPA，以便後續能夠實現真實持久化。

#### Acceptance Criteria

1. WHEN 檢查 `application.yml` THEN 系統 SHALL 包含 H2 datasource 配置
2. WHEN 檢查 `application.yml` THEN 系統 SHALL 包含 JPA 配置（允許 `ddl-auto: create-drop`）
3. WHEN 應用程式啟動 THEN 系統 SHALL 不因 datasource/JPA 報錯

### Requirement 5: Actuator Health Endpoint

**User Story:** 作為運維人員，我需要健康檢查端點，以便監控應用程式狀態。

#### Acceptance Criteria

1. WHEN 發送 `GET /actuator/health` THEN 系統 SHALL 回傳 HTTP 200
2. WHEN 發送 `GET /actuator/health` THEN 系統 SHALL 回傳狀態為 UP

## Non-Functional Requirements

### Code Architecture and Modularity
- **Single Responsibility Principle**: 每個配置檔案有明確單一職責
- **Modular Design**: 配置分離（application.yml 負責運行時配置）
- **Clear Interfaces**: Gradle 依賴清晰分類（implementation vs testImplementation）

### Performance
- 應用程式啟動時間應在合理範圍內（< 10 秒）
- Virtual Threads 必須正確啟用

### Security
- Level 0 不實作安全機制（明確禁止 Spring Security）

### Reliability
- `./gradlew test` 必須穩定通過
- `./gradlew bootRun` 必須穩定啟動

### Usability
- 專案結構清晰，符合 Spring Boot 標準慣例
