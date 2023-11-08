package io.github.guardjo.pharmacyexplorer.repository.cache;

import io.github.guardjo.pharmacyexplorer.AbstractTestContainerTest;
import io.github.guardjo.pharmacyexplorer.domain.Pharmacy;
import io.github.guardjo.pharmacyexplorer.domain.cache.PharmacyCache;
import io.github.guardjo.pharmacyexplorer.util.TestDataGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ActiveProfiles("test")
@DataRedisTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PharmacyCacheRepositoryTest extends AbstractTestContainerTest {
    @Autowired
    private PharmacyCacheRepository pharmacyCacheRepository;

    private final List<Long> testDataIds = new ArrayList<>();

    private final int TEST_DATA_SZIE = 10;

    @BeforeEach
    void setUp() {
        for (int i = 0; i < TEST_DATA_SZIE; i++) {
            Pharmacy pharmacy = TestDataGenerator.pharmacy(i);
            PharmacyCache pharmacyCache = PharmacyCache.builder()
                    .id(pharmacy.getId())
                    .cacheData(pharmacy)
                    .build();

            testDataIds.add(pharmacyCache.getId());

            pharmacyCacheRepository.save(pharmacyCache);
        }
    }

    @DisplayName("특정 PharmacyCache 조회")
    @Test
    void test_findById() {
        long cacheId = testDataIds.get(0);

        PharmacyCache actual = pharmacyCacheRepository.findById(cacheId).orElseThrow();

        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isEqualTo(cacheId);
    }

    @DisplayName("특정 PharmacyCache 삭제")
    @Test
    void test_deleteById() {
        long cacheId = testDataIds.get(1);

        pharmacyCacheRepository.deleteById(cacheId);

        assertThat(pharmacyCacheRepository.count()).isEqualTo(TEST_DATA_SZIE - 1);
    }
}