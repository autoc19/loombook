# Tasks Document - Platform Bootstrap Level 0

- [x] 1. 建立 Gradle 專案骨架與版本鎖定
  - Files: build.gradle.kts, settings.gradle.kts, gradle.properties
  - 配置 Java 21、Spring Boot 3.4.13
  - 添加必要依賴：web, data-jpa, h2, actuator, test, archunit-junit5
  - Purpose: 建立可編譯的專案基礎
  - _Requirements: 1_
  - _Prompt: Implement the task for spec platform-bootstrap-level0, first run spec-workflow-guide to get the workflow guide then implement the task: Role: Java/Gradle Build Engineer | Task: 建立 Gradle 專案骨架，配置 Java 21 與 Spring Boot 3.4.13，添加所有必要依賴（web, data-jpa, h2, actuator, test, archunit-junit5），確保版本透過 gradle.properties 鎖定 | Restrictions: 必須使用 Kotlin DSL (build.gradle.kts)，不得引入未要求的依賴，Spring Boot 版本必須為 3.4.13 | Success: ./gradlew build 成功執行，所有依賴正確解析 | After completion: Mark task as [-] in tasks.md before starting, use log-implementation tool to record details, then mark as [x]_

- [x] 2. 建立 Spring Boot 啟動類與基本啟動測試
  - Files: src/main/java/com/loombook/LoombookApplication.java, src/test/java/com/loombook/LoombookApplicationTests.java
  - 建立 @SpringBootApplication 啟動類
  - 建立 @SpringBootTest 驗證 Context 載入
  - Purpose: 確保應用程式可啟動
  - _Requirements: 2_
  - _Prompt: Implement the task for spec platform-bootstrap-level0, first run spec-workflow-guide to get the workflow guide then implement the task: Role: Spring Boot Developer | Task: 建立 LoombookApplication.java 作為應用程式入口（@SpringBootApplication），建立 LoombookApplicationTests.java 驗證 Spring Context 可正確載入 | Restrictions: 啟動類必須在 com.loombook 包下，測試必須使用 @SpringBootTest，不得添加額外業務邏輯 | Success: ./gradlew test 通過，Context 載入測試綠燈 | After completion: Mark task as [-] in tasks.md before starting, use log-implementation tool to record details, then mark as [x]_

- [x] 3. 建立 application.yml 並啟用 Virtual Threads
  - File: src/main/resources/application.yml
  - 配置 server.port: 8080
  - 配置 spring.threads.virtual.enabled: true
  - Purpose: 啟用 Java 21 Virtual Threads 並發模型
  - _Requirements: 3_
  - _Prompt: Implement the task for spec platform-bootstrap-level0, first run spec-workflow-guide to get the workflow guide then implement the task: Role: Spring Boot Configuration Specialist | Task: 建立 application.yml，配置 server.port 為 8080，啟用 Virtual Threads (spring.threads.virtual.enabled: true) | Restrictions: 必須使用 YAML 格式，不得使用 properties 格式，配置必須符合 Spring Boot 3.x 語法 | Success: 應用程式啟動時使用 Virtual Threads | After completion: Mark task as [-] in tasks.md before starting, use log-implementation tool to record details, then mark as [x]_

- [x] 4. 配置 H2 datasource + JPA 最小配置
  - File: src/main/resources/application.yml (修改)
  - 添加 H2 in-memory datasource 配置
  - 添加 JPA 配置（ddl-auto: create-drop）
  - Purpose: 為後續持久化功能準備基礎
  - _Requirements: 4_
  - _Prompt: Implement the task for spec platform-bootstrap-level0, first run spec-workflow-guide to get the workflow guide then implement the task: Role: Spring Data JPA Specialist | Task: 在 application.yml 中添加 H2 in-memory datasource 配置與 JPA 最小配置，使用 ddl-auto: create-drop | Restrictions: 必須使用 H2 in-memory 模式，不得使用檔案模式，JPA 配置必須最小化 | Success: 應用程式啟動時不因 datasource/JPA 報錯 | After completion: Mark task as [-] in tasks.md before starting, use log-implementation tool to record details, then mark as [x]_

- [x] 5. 啟用 Actuator 並驗證 health endpoint
  - File: src/main/resources/application.yml (修改，如需要)
  - 確保 spring-boot-starter-actuator 生效
  - 驗證 GET /actuator/health 回傳 200 UP
  - Purpose: 提供最小可觀測性能力
  - _Requirements: 5_
  - _Prompt: Implement the task for spec platform-bootstrap-level0, first run spec-workflow-guide to get the workflow guide then implement the task: Role: Spring Boot Actuator Specialist | Task: 確保 Actuator 正確配置，驗證 GET /actuator/health 端點可訪問且回傳 200 狀態與 UP 狀態 | Restrictions: 使用 Actuator 預設配置即可，不得暴露敏感端點，不得添加額外 metrics/tracing | Success: curl http://localhost:8080/actuator/health 回傳 {"status":"UP"} | After completion: Mark task as [-] in tasks.md before starting, use log-implementation tool to record details, then mark as [x]_

- [x] 6. 最終驗收：DoD 命令驗證
  - 執行 ./gradlew test 確認全綠
  - 執行 ./gradlew bootRun 確認可啟動
  - 驗證 GET /actuator/health 回傳 UP
  - Purpose: 確保 spec 交付標準達成
  - _Requirements: 1, 2, 3, 4, 5_
  - _Prompt: Implement the task for spec platform-bootstrap-level0, first run spec-workflow-guide to get the workflow guide then implement the task: Role: QA Engineer | Task: 執行最終驗收，確認 ./gradlew test 全綠、./gradlew bootRun 可啟動、GET /actuator/health 回傳 UP | Restrictions: 不得修改任何程式碼，僅執行驗證 | Success: 所有 DoD 命令通過，spec 完成 | After completion: Mark task as [-] in tasks.md before starting, use log-implementation tool to record details, then mark as [x]_
