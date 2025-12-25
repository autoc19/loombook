# Tasks Document - Architecture Guardrails Level 0

- [x] 1. 建立六角架構 package 結構
  - Files: 8 個 package 目錄，每個包含 package-info.java
  - 建立 domain.model, domain.port
  - 建立 application.command, application.query
  - 建立 infrastructure.persistence.jpa.entity, repository, adapter
  - 建立 api.rest
  - Purpose: 為後續開發提供明確的程式碼落地位置
  - _Requirements: 1_
  - _Prompt: Implement the task for spec architecture-guardrails-level0, first run spec-workflow-guide to get the workflow guide then implement the task: Role: Java Architect | Task: 建立六角架構的 8 個必要 package，每個 package 包含 package-info.java 以確保目錄存在並提供文檔說明 | Restrictions: package 命名必須完全符合 steering 規範，不得添加額外 package，package-info.java 必須包含 Javadoc 說明該層職責 | Success: 所有 8 個 package 存在且包含 package-info.java | After completion: Mark task as [-] in tasks.md before starting, use log-implementation tool to record details, then mark as [x]_

- [x] 2. 新增 ArchUnit 測試 - Rule A (Domain 不依賴 Spring)
  - File: src/test/java/com/loombook/ArchitectureTest.java
  - 實作規則：com.loombook.domain.. 不得依賴 org.springframework..
  - 添加清晰的規則註解說明
  - Purpose: 確保 Domain 層框架無關
  - _Requirements: 2_
  - _Prompt: Implement the task for spec architecture-guardrails-level0, first run spec-workflow-guide to get the workflow guide then implement the task: Role: ArchUnit Specialist | Task: 建立 ArchitectureTest.java，實作 Rule A 確保 com.loombook.domain.. 不依賴 org.springframework..，添加清晰的 Javadoc 說明規則目的 | Restrictions: 必須使用 ArchUnit JUnit5 整合，規則必須涵蓋所有 domain 子包，不得使用過於寬鬆的規則 | Success: ./gradlew test 通過，且當 Domain 引入 Spring 時測試失敗 | After completion: Mark task as [-] in tasks.md before starting, use log-implementation tool to record details, then mark as [x]_

- [x] 3. 新增 ArchUnit 測試 - Rule B (API 不依賴 Infrastructure)
  - File: src/test/java/com/loombook/ArchitectureTest.java (修改)
  - 實作規則：com.loombook.api.. 不得依賴 com.loombook.infrastructure..
  - 添加清晰的規則註解說明
  - Purpose: 確保 API 層與 Infrastructure 層隔離
  - _Requirements: 3_
  - _Prompt: Implement the task for spec architecture-guardrails-level0, first run spec-workflow-guide to get the workflow guide then implement the task: Role: ArchUnit Specialist | Task: 在 ArchitectureTest.java 中添加 Rule B，確保 com.loombook.api.. 不依賴 com.loombook.infrastructure..，添加清晰的 Javadoc 說明規則目的 | Restrictions: 必須與 Rule A 在同一測試類別中，規則必須涵蓋所有 api 子包，不得使用過於寬鬆的規則 | Success: ./gradlew test 通過，且當 API 引入 Infrastructure 時測試失敗 | After completion: Mark task as [-] in tasks.md before starting, use log-implementation tool to record details, then mark as [x]_
