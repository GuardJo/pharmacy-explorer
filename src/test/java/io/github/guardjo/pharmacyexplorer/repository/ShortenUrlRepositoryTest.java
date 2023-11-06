package io.github.guardjo.pharmacyexplorer.repository;

import io.github.guardjo.pharmacyexplorer.AbstractTestContainerTest;
import io.github.guardjo.pharmacyexplorer.config.JpaConfig;
import io.github.guardjo.pharmacyexplorer.domain.ShortenUrl;
import io.github.guardjo.pharmacyexplorer.util.TestDataGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ActiveProfiles("test")
@DataJpaTest
@Import(JpaConfig.class)
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ShortenUrlRepositoryTest extends AbstractTestContainerTest {
    @Autowired
    private ShortenUrlRepository shortenUrlRepository;

    private final Map<Long, String> testDataMap = new HashMap<>();

    @BeforeEach
    void setUp() {
        for (int i = 0; i < 10; i++) {
            ShortenUrl testData = shortenUrlRepository.save(TestDataGenerator.shortenUrl());

            testDataMap.put(testData.getId(), testData.getOriginalUrl());
        }
    }

    @DisplayName("특정 데이터 조회")
    @Test
    void test_findById() {
        testDataMap.keySet().forEach((key) -> {
            ShortenUrl shortenUrl = shortenUrlRepository.findById(key).orElseThrow();

            assertThat(shortenUrl.getOriginalUrl()).isEqualTo(testDataMap.get(key));
        });
    }
}