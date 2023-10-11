package io.github.guardjo.pharmacyexplorer.repository;

import io.github.guardjo.pharmacyexplorer.AbstractTestContainerTest;
import io.github.guardjo.pharmacyexplorer.config.JpaConfig;
import io.github.guardjo.pharmacyexplorer.domain.Pharmacy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ActiveProfiles("test")
@DataJpaTest
@Import(value = JpaConfig.class)
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PharmacyRepositoryTest extends AbstractTestContainerTest {
    @Autowired
    private PharmacyRepository pharmacyRepository;

    private final static int TEST_DATA_SIZE = 10;
    private List<Long> testDataIds = new ArrayList<>();

    @BeforeEach
    void setUp() {
        List<Pharmacy> pharmacies = new ArrayList<>();

        for (int i = 0; i < TEST_DATA_SIZE; i++) {
            Pharmacy pharmacy = Pharmacy.builder()
                    .name("test " + i)
                    .address("test address " + i)
                    .build();
            pharmacies.add(pharmacy);
        }

        testDataIds = pharmacyRepository.saveAll(pharmacies).stream()
                .map(Pharmacy::getId)
                .collect(Collectors.toList());
    }

    @DisplayName("신규 약국 정보 저장")
    @Test
    void test_save() {
        Pharmacy newPharmacy = Pharmacy.builder()
                .name("test")
                .address("test address")
                .build();

        LocalDateTime now = LocalDateTime.now();
        Pharmacy actual = pharmacyRepository.save(newPharmacy);

        assertThat(actual.getId()).isNotNull();
        assertThat(actual.getCreatedDate()).isAfter(now);
        assertThat(actual.getModifiedDate()).isAfter(now);
    }

    @DisplayName("특정 약국 정보 반환")
    @Test
    void test_findById() {
        long pharmacyId = testDataIds.get(0);

        Pharmacy actual = pharmacyRepository.findById(pharmacyId).orElseThrow();

        assertThat(actual.getId()).isEqualTo(pharmacyId);
    }

    @DisplayName("저장되어 있는 전체 약국 정보 반환")
    @Test
    void test_findAll() {
        List<Pharmacy> pharmacies = pharmacyRepository.findAll();

        assertThat(pharmacies.size()).isEqualTo(TEST_DATA_SIZE);
    }

    @DisplayName("특정 약국 데이터 삭제")
    @Test
    void test_deleteById() {
        long pharmacyId = testDataIds.get(0);

        pharmacyRepository.deleteById(pharmacyId);

        assertThat(pharmacyRepository.count()).isEqualTo(TEST_DATA_SIZE - 1);
    }
}