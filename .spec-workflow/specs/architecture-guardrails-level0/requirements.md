# Requirements Document - Architecture Guardrails Level 0

## Introduction

本 spec 負責建立六角架構的包結構與 ArchUnit 守門規則，確保後續開發不會偏離架構邊界。這是 Level 0 的第二個 spec，依賴於 platform-bootstrap-level0 完成。

## Alignment with Product Vision

根據 product.md，Level 0 需要「驗證六角架構/DDD 邊界與依賴方向」。本 spec 直接實現這個目標：
- 建立六角架構的標準包結構
- 透過 ArchUnit 自動化守門，防止架構腐化
- 為後續商品閉環 spec 提供明確的程式碼落地位置

## Requirements

### Requirement 1: 六角架構包結構

**User Story:** 作為開發者，我需要明確的包結構，以便知道程式碼應該放在哪裡。

#### Acceptance Criteria

1. WHEN 檢查專案結構 THEN 系統 SHALL 存在以下 package：
   - `com.loombook.domain.model`
   - `com.loombook.domain.port`
   - `com.loombook.application.command`
   - `com.loombook.application.query`
   - `com.loombook.infrastructure.persistence.jpa.entity`
   - `com.loombook.infrastructure.persistence.jpa.repository`
   - `com.loombook.infrastructure.persistence.jpa.adapter`
   - `com.loombook.api.rest`

2. WHEN package 為空 THEN 系統 SHALL 包含 package-info.java 或 .gitkeep 以確保目錄存在

### Requirement 2: ArchUnit 守門規則 - Domain 獨立性

**User Story:** 作為架構師，我需要確保 Domain 層不依賴 Spring 框架，以便 Domain 可以獨立演進。

#### Acceptance Criteria

1. WHEN Domain 層程式碼引入 `org.springframework` THEN ArchUnit 測試 SHALL 失敗
2. WHEN 執行 `./gradlew test` THEN ArchUnit Rule A SHALL 被驗證
3. IF Domain 層違反規則 THEN 系統 SHALL 在測試階段報錯並阻止構建

### Requirement 3: ArchUnit 守門規則 - API 層隔離

**User Story:** 作為架構師，我需要確保 API 層不直接依賴 Infrastructure 層，以便保持分層清晰。

#### Acceptance Criteria

1. WHEN API 層程式碼引入 `com.loombook.infrastructure` THEN ArchUnit 測試 SHALL 失敗
2. WHEN 執行 `./gradlew test` THEN ArchUnit Rule B SHALL 被驗證
3. IF API 層違反規則 THEN 系統 SHALL 在測試階段報錯並阻止構建

### Requirement 4: 依賴方向文檔化

**User Story:** 作為開發者，我需要清楚的依賴方向說明，以便遵循架構規範。

#### Acceptance Criteria

1. WHEN 檢查 ArchitectureTest THEN 系統 SHALL 包含清晰的規則註解說明依賴方向
2. WHEN 新開發者加入 THEN 系統 SHALL 透過測試失敗自動提示架構違規

## Non-Functional Requirements

### Code Architecture and Modularity
- **Single Responsibility Principle**: 每個 package 有明確單一職責
- **Modular Design**: 六角架構四層分離（Domain / Application / Infrastructure / API）
- **Dependency Management**: 依賴方向嚴格單向（外層依賴內層）
- **Clear Interfaces**: Domain Port 定義清晰的介面契約

### Performance
- ArchUnit 測試執行時間應在合理範圍內（< 5 秒）

### Security
- Level 0 不實作安全機制

### Reliability
- ArchUnit 測試必須穩定可重複執行
- 規則不得產生誤報

### Usability
- 測試失敗訊息應清楚指出違規位置與原因
