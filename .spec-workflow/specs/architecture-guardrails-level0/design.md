# Design Document - Architecture Guardrails Level 0

## Overview

本設計文檔描述如何建立六角架構的包結構與 ArchUnit 守門規則。透過自動化測試確保架構邊界不被破壞，為後續商品閉環 spec 提供明確的程式碼落地位置。

## Steering Document Alignment

### Technical Standards (tech.md)
- 六角架構 + DDD 最小集合
- Domain 層不可依賴 Spring/JPA/Web
- ArchUnit 最小兩條規則

### Project Structure (structure.md)
- 必須存在 8 個標準 package
- 依賴方向：api → application → domain，infrastructure → domain

## Code Reuse Analysis

### Existing Components to Leverage
- **platform-bootstrap-level0**: 專案骨架與 ArchUnit 依賴已就位

### Integration Points
- ArchUnit JUnit5 整合
- Gradle test task

## Architecture

六角架構分層與依賴方向：

```
┌─────────────────────────────────────────────────────────┐
│                      API Layer                          │
│                   (api.rest)                            │
│                        │                                │
│                        ▼                                │
│              Application Layer                          │
│        (application.command / query)                    │
│                        │                                │
│                        ▼                                │
│                  Domain Layer                           │
│            (domain.model / port)                        │
│                        ▲                                │
│                        │                                │
│             Infrastructure Layer                        │
│    (infrastructure.persistence.jpa.*)                   │
└─────────────────────────────────────────────────────────┘
```

依賴規則：
- Domain 不依賴任何外層
- Application 只依賴 Domain
- API 只依賴 Application（不得依賴 Infrastructure）
- Infrastructure 實作 Domain Port

## Components and Interfaces

### Component 1: Package Structure
- **Purpose:** 定義六角架構的程式碼組織
- **Packages:**
  - `com.loombook.domain.model` - 領域模型（Aggregate, Value Object）
  - `com.loombook.domain.port` - 領域埠（Repository 介面）
  - `com.loombook.application.command` - 命令用例
  - `com.loombook.application.query` - 查詢用例
  - `com.loombook.infrastructure.persistence.jpa.entity` - JPA Entity
  - `com.loombook.infrastructure.persistence.jpa.repository` - Spring Data Repository
  - `com.loombook.infrastructure.persistence.jpa.adapter` - Port Adapter
  - `com.loombook.api.rest` - REST Controller

### Component 2: ArchitectureTest
- **Purpose:** 自動化架構守門
- **File:** src/test/java/com/loombook/ArchitectureTest.java
- **Rules:**
  - Rule A: Domain 不依賴 Spring
  - Rule B: API 不依賴 Infrastructure

## Data Models

本 spec 不定義業務資料模型。

## Error Handling

### Error Scenarios
1. **架構違規 - Domain 依賴 Spring**
   - **Handling:** ArchUnit 測試失敗，顯示違規類別與依賴
   - **User Impact:** 構建失敗，必須修正後才能繼續

2. **架構違規 - API 依賴 Infrastructure**
   - **Handling:** ArchUnit 測試失敗，顯示違規類別與依賴
   - **User Impact:** 構建失敗，必須修正後才能繼續

## Testing Strategy

### Unit Testing
- ArchitectureTest：驗證架構規則

### Integration Testing
- 本 spec 不需要整合測試

### End-to-End Testing
- Level 0 明確禁止 E2E 測試
