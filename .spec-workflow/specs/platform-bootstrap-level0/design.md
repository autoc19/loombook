# Design Document - Platform Bootstrap Level 0

## Overview

本設計文檔描述如何建立「企業級線上書店與交易平台」的專案骨架。這是一個 Spring Boot 3.4.13 + Java 21 專案，採用 Gradle 構建，啟用 Virtual Threads，並配置 H2 + JPA 作為持久化基礎。

## Steering Document Alignment

### Technical Standards (tech.md)
- Java 21 (LTS)
- Spring Boot 3.4.13（透過 gradle.properties 鎖定）
- Spring MVC（同步）+ Virtual Threads
- H2 in-memory + Spring Data JPA
- Spring Boot Actuator

### Project Structure (structure.md)
- 專案根目錄包含標準 Gradle 檔案
- `src/main/java/com/loombook/` 為主程式碼目錄
- `src/main/resources/` 包含配置檔案
- `src/test/java/` 包含測試程式碼

## Code Reuse Analysis

### Existing Components to Leverage
- 無（這是專案初始化 spec）

### Integration Points
- Spring Boot Auto-configuration
- H2 Database（in-memory）
- Spring Data JPA
- Spring Boot Actuator

## Architecture

本 spec 建立專案基礎設施，不涉及業務邏輯架構。主要產出為：

```
project-root/
├── build.gradle.kts
├── settings.gradle.kts
├── gradle.properties
├── gradlew / gradlew.bat
├── src/
│   ├── main/
│   │   ├── java/com/loombook/
│   │   │   └── LoombookApplication.java
│   │   └── resources/
│   │       └── application.yml
│   └── test/
│       └── java/com/loombook/
│           └── LoombookApplicationTests.java
```

## Components and Interfaces

### Component 1: Gradle Build Configuration
- **Purpose:** 定義專案依賴、版本與構建配置
- **Files:** build.gradle.kts, settings.gradle.kts, gradle.properties
- **Dependencies:** Spring Boot Plugin, Java 21

### Component 2: Spring Boot Application
- **Purpose:** 應用程式入口點
- **File:** LoombookApplication.java
- **Interfaces:** main() 方法
- **Dependencies:** spring-boot-starter-web

### Component 3: Application Configuration
- **Purpose:** 運行時配置（端口、Virtual Threads、資料庫）
- **File:** application.yml
- **Dependencies:** 無

### Component 4: Application Startup Test
- **Purpose:** 驗證 Spring Context 可正確載入
- **File:** LoombookApplicationTests.java
- **Dependencies:** spring-boot-starter-test

## Data Models

本 spec 不定義業務資料模型。H2 + JPA 配置僅為後續 spec 準備基礎。

## Error Handling

### Error Scenarios
1. **Gradle 依賴解析失敗**
   - **Handling:** 檢查網路連線與 Maven Central 可用性
   - **User Impact:** 無法編譯專案

2. **Spring Context 載入失敗**
   - **Handling:** 檢查配置檔案語法與依賴版本
   - **User Impact:** 應用程式無法啟動

3. **H2 資料庫初始化失敗**
   - **Handling:** 檢查 JPA 配置
   - **User Impact:** 應用程式啟動失敗

## Testing Strategy

### Unit Testing
- LoombookApplicationTests：驗證 Spring Context 載入

### Integration Testing
- 本 spec 不需要整合測試

### End-to-End Testing
- Level 0 明確禁止 E2E 測試
