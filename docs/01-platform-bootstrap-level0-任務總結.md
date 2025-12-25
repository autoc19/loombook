# 01 - platform-bootstrap-level0 任務總結

## 概述

本文檔記錄 `platform-bootstrap-level0` spec 的完整實現過程，包括任務執行順序、遇到的問題、解決方案及最終驗收結果。

**執行時間**：2025-12-25
**總任務數**：6 個
**完成狀態**：✅ 全部完成

---

## 任務執行詳細記錄

### 任務 1：建立 Gradle 專案骨架與版本鎖定

**目標**：
- 建立 build.gradle.kts, settings.gradle.kts, gradle.properties
- 配置 Java 21、Spring Boot 3.4.13
- 添加必要依賴：web, data-jpa, h2, actuator, test, archunit-junit5

**執行過程**：

1. **第一次嘗試**：建立 build.gradle.kts，使用 `${property("springBootVersion")}` 語法從 gradle.properties 讀取版本號

2. **遇到問題**：執行 `.\gradlew.bat build --dry-run` 時報錯
   ```
   e: file:///D:/DEV/loombook/build.gradle.kts:3:47: Unresolved reference. 
   None of the following candidates is applicable because of receiver type mismatch
   ```
   原因：在 plugins 區塊中無法使用 `property()` 函數

3. **解決方案**：修改 build.gradle.kts，將版本號直接寫入 plugins 區塊
   ```kotlin
   id("org.springframework.boot") version "3.4.13"
   ```
   ArchUnit 版本則使用 `val archunitVersion = "1.3.0"` 在 dependencies 區塊外定義

4. **Gradle Wrapper 問題**：系統未安裝 Gradle，執行 `gradle wrapper` 失敗
   ```
   gradle : The term 'gradle' is not recognized as the name of a cmdlet
   ```

5. **解決方案**：手動下載 Gradle Wrapper 檔案
   - 建立 `gradle/wrapper/gradle-wrapper.properties`
   - 從 GitHub 下載 `gradlew`、`gradlew.bat`、`gradle-wrapper.jar`

6. **驗證成功**：
   ```
   .\gradlew.bat --version
   Gradle 8.11
   
   .\gradlew.bat build --dry-run
   BUILD SUCCESSFUL in 1m 7s
   ```

**產出檔案**：
- `build.gradle.kts`
- `settings.gradle.kts`
- `gradle.properties`
- `gradle/wrapper/gradle-wrapper.properties`
- `gradle/wrapper/gradle-wrapper.jar`
- `gradlew`
- `gradlew.bat`

---

### 任務 2：建立 Spring Boot 啟動類與基本啟動測試

**目標**：
- 建立 `LoombookApplication.java`（@SpringBootApplication）
- 建立 `LoombookApplicationTests.java`（@SpringBootTest）

**執行過程**：

1. 建立 `src/main/java/com/loombook/LoombookApplication.java`
   ```java
   @SpringBootApplication
   public class LoombookApplication {
       public static void main(String[] args) {
           SpringApplication.run(LoombookApplication.class, args);
       }
   }
   ```

2. 建立 `src/test/java/com/loombook/LoombookApplicationTests.java`
   ```java
   @SpringBootTest
   class LoombookApplicationTests {
       @Test
       void contextLoads() {
       }
   }
   ```

**遇到問題**：無

**產出檔案**：
- `src/main/java/com/loombook/LoombookApplication.java`
- `src/test/java/com/loombook/LoombookApplicationTests.java`

---

### 任務 3：建立 application.yml 並啟用 Virtual Threads

**目標**：
- 配置 `server.port: 8080`
- 配置 `spring.threads.virtual.enabled: true`

**執行過程**：

1. 建立 `src/main/resources/application.yml`，包含：
   ```yaml
   server:
     port: 8080
   spring:
     threads:
       virtual:
         enabled: true
   ```

**遇到問題**：無

**產出檔案**：
- `src/main/resources/application.yml`

---

### 任務 4：配置 H2 datasource + JPA 最小配置

**目標**：
- 添加 H2 in-memory datasource 配置
- 添加 JPA 配置（ddl-auto: create-drop）

**執行過程**：

1. 在 `application.yml` 中添加：
   ```yaml
   spring:
     datasource:
       url: jdbc:h2:mem:loombook
       driver-class-name: org.h2.Driver
       username: sa
       password:
     jpa:
       hibernate:
         ddl-auto: create-drop
       show-sql: false
     h2:
       console:
         enabled: false
   ```

**遇到問題**：無

**產出檔案**：
- `src/main/resources/application.yml`（修改）

---

### 任務 5：啟用 Actuator 並驗證 health endpoint

**目標**：
- 確保 spring-boot-starter-actuator 生效
- 驗證 GET /actuator/health 回傳 200 UP

**執行過程**：

1. 在 `application.yml` 中添加：
   ```yaml
   management:
     endpoints:
       web:
         exposure:
           include: health
   ```

2. Actuator 依賴已在任務 1 的 build.gradle.kts 中配置

**遇到問題**：無

**產出檔案**：
- `src/main/resources/application.yml`（修改）

---

### 任務 6：最終驗收 - DoD 命令驗證

**目標**：
- 執行 `./gradlew test` 確認全綠
- 執行 `./gradlew bootRun` 確認可啟動
- 驗證 GET /actuator/health 回傳 UP

**執行過程**：

1. **執行測試**：
   ```
   .\gradlew.bat test
   BUILD SUCCESSFUL in 28s
   4 actionable tasks: 4 executed
   ```

2. **啟動應用程式**：
   ```
   .\gradlew.bat bootRun
   Started LoombookApplication in 2.303 seconds (process running for 2.551)
   Tomcat started on port 8080 (http) with context path '/'
   Exposing 1 endpoint beneath base path '/actuator'
   ```

3. **驗證 Health Endpoint**：
   ```
   Invoke-RestMethod -Uri "http://localhost:8080/actuator/health"
   {"status":"UP"}
   ```

**遇到問題**：無

**驗收結果**：✅ 全部通過

---

## 完成的測試

| 測試類別 | 測試名稱 | 結果 |
|---------|---------|------|
| Spring Context | LoombookApplicationTests.contextLoads() | ✅ 通過 |

---

## 最終產出檔案清單

| 檔案路徑 | 用途 |
|---------|------|
| `build.gradle.kts` | Gradle 構建配置（Kotlin DSL） |
| `settings.gradle.kts` | Gradle 專案設定 |
| `gradle.properties` | Gradle 屬性配置 |
| `gradle/wrapper/gradle-wrapper.properties` | Gradle Wrapper 配置 |
| `gradle/wrapper/gradle-wrapper.jar` | Gradle Wrapper JAR |
| `gradlew` | Unix Gradle Wrapper 腳本 |
| `gradlew.bat` | Windows Gradle Wrapper 腳本 |
| `src/main/java/com/loombook/LoombookApplication.java` | Spring Boot 啟動類 |
| `src/test/java/com/loombook/LoombookApplicationTests.java` | Spring Context 載入測試 |
| `src/main/resources/application.yml` | 應用程式配置 |

---

## 技術配置摘要

### build.gradle.kts 依賴
```kotlin
dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    runtimeOnly("com.h2database:h2")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("com.tngtech.archunit:archunit-junit5:1.3.0")
}
```

### application.yml 完整配置
```yaml
server:
  port: 8080

spring:
  application:
    name: loombook
  threads:
    virtual:
      enabled: true
  datasource:
    url: jdbc:h2:mem:loombook
    driver-class-name: org.h2.Driver
    username: sa
    password:
  jpa:
    hibernate:
      ddl-auto: create-drop
    show-sql: false
  h2:
    console:
      enabled: false

management:
  endpoints:
    web:
      exposure:
        include: health
```

---

## 問題與解決方案總結

| # | 問題描述 | 解決方案 | 結果 |
|---|---------|---------|------|
| 1 | plugins 區塊無法使用 property() 函數 | 直接在 plugins 區塊寫入版本號 | ✅ 解決 |
| 2 | 系統未安裝 Gradle | 手動下載 Gradle Wrapper 檔案 | ✅ 解決 |

---

## 結論

`platform-bootstrap-level0` spec 已成功完成，建立了可運行、可測試的 Spring Boot 專案骨架。所有 DoD（Definition of Done）驗收標準均已通過：

- ✅ `./gradlew test` 全綠
- ✅ `./gradlew bootRun` 可啟動（8080 端口）
- ✅ `GET /actuator/health` 回傳 `{"status":"UP"}`
- ✅ Java 21 + Spring Boot 3.4.13 配置正確
- ✅ Virtual Threads 已啟用
- ✅ H2 + JPA 配置就位
- ✅ ArchUnit 依賴已添加（供下一個 spec 使用）

下一步：執行 `architecture-guardrails-level0` spec。
