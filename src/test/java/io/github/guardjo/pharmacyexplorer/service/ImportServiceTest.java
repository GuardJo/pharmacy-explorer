package io.github.guardjo.pharmacyexplorer.service;

import io.github.guardjo.pharmacyexplorer.AbstractTestContainerTest;
import io.github.guardjo.pharmacyexplorer.config.CsvConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;

@ActiveProfiles("test")
@SpringBootTest
@Import(CsvConfig.class)
@Testcontainers
class ImportServiceTest extends AbstractTestContainerTest {
    @Autowired
    private ImportService importService;

    @DisplayName("약국 초기 데이터 적재")
    @Test
    void test_initPharmacyData() {
        assertThatCode(() -> importService.initPharmacyData()).doesNotThrowAnyException();
    }
}