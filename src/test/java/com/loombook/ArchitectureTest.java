package com.loombook;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

/**
 * 架構守門測試 - 使用 ArchUnit 驗證六角架構邊界
 * 
 * <p>本測試類別確保專案遵循六角架構的依賴規則：
 * <ul>
 *   <li>Rule A: Domain 層不得依賴 Spring Framework</li>
 *   <li>Rule B: API 層不得依賴 Infrastructure 層</li>
 * </ul>
 */
class ArchitectureTest {

    private static JavaClasses importedClasses;

    @BeforeAll
    static void setup() {
        importedClasses = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages("com.loombook");
    }

    /**
     * Rule A: Domain 層必須保持框架無關
     * 
     * <p>Domain 層（com.loombook.domain..）不得依賴 Spring Framework（org.springframework..）。
     * 這確保領域模型可以獨立於框架演進，並且可以在不同的技術棧中重用。
     * 
     * <p>注意：allowEmptyShould(true) 允許在 Domain 層尚無類別時測試通過，
     * 當有類別加入時規則會自動生效。
     */
    @Test
    @DisplayName("Rule A: Domain 層不得依賴 Spring Framework")
    void domainShouldNotDependOnSpring() {
        ArchRule rule = noClasses()
                .that().resideInAPackage("com.loombook.domain..")
                .should().dependOnClassesThat().resideInAPackage("org.springframework..")
                .allowEmptyShould(true);

        rule.check(importedClasses);
    }

    /**
     * Rule B: API 層必須與 Infrastructure 層隔離
     * 
     * <p>API 層（com.loombook.api..）不得依賴 Infrastructure 層（com.loombook.infrastructure..）。
     * API 層應該只依賴 Application 層（UseCase），透過 UseCase 間接使用 Infrastructure。
     * 這確保 API 層保持薄且專注於 HTTP 協議轉換。
     * 
     * <p>注意：allowEmptyShould(true) 允許在 API 層尚無類別時測試通過，
     * 當有類別加入時規則會自動生效。
     */
    @Test
    @DisplayName("Rule B: API 層不得依賴 Infrastructure 層")
    void apiShouldNotDependOnInfrastructure() {
        ArchRule rule = noClasses()
                .that().resideInAPackage("com.loombook.api..")
                .should().dependOnClassesThat().resideInAPackage("com.loombook.infrastructure..")
                .allowEmptyShould(true);

        rule.check(importedClasses);
    }
}
