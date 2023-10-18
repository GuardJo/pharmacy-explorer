package io.github.guardjo.pharmacyexplorer.repository;

import io.github.guardjo.pharmacyexplorer.AbstractTestContainerTest;
import io.github.guardjo.pharmacyexplorer.config.JpaConfig;
import io.github.guardjo.pharmacyexplorer.domain.SearchInfo;
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

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ActiveProfiles("test")
@DataJpaTest
@Import(JpaConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
class SearchInfoRepositoryTest extends AbstractTestContainerTest {
    @Autowired
    private SearchInfoRepository searchInfoRepository;

    private final int TEST_DATA_SIZE = 10;
    private final List<SearchInfo> TEST_DATA_LIST = new ArrayList<>();

    @BeforeEach
    void setUp() {
        for (int i = 0; i < TEST_DATA_SIZE; i++) {
            TEST_DATA_LIST.add(TestDataGenerator.searchInfo(i + 10, i + 10));
        }

        searchInfoRepository.saveAll(TEST_DATA_LIST);
    }

    @DisplayName("특정 searchInfo 조회")
    @Test
    void test_findOne() {
        SearchInfo expected = TEST_DATA_LIST.get(0);
        long id = expected.getId();

        SearchInfo actual = searchInfoRepository.findById(id).orElseThrow();

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("전체 searchInfo 조회")
    @Test
    void test_findAll() {
        List<SearchInfo> actual = searchInfoRepository.findAll();

        assertThat(actual.size()).isEqualTo(TEST_DATA_SIZE);
    }

    @DisplayName("특정 searchInfo 삭제")
    @Test
    void test_deleteById() {
        long id = TEST_DATA_LIST.get(0).getId();

        searchInfoRepository.deleteById(id);

        assertThat(searchInfoRepository.count()).isEqualTo(TEST_DATA_SIZE - 1);
    }

    @DisplayName("특정 위도, 경도에 해당하는 searchInfo 조회")
    @Test
    void test_findByBaseLatAndBaseLng() {
        SearchInfo expected = TEST_DATA_LIST.get(0);
        double baseLat = expected.getBaseLat();
        double baseLng = expected.getBaseLng();

        SearchInfo actual = searchInfoRepository.findByBaseLatAndBaseLng(baseLat, baseLng).orElseThrow();

        assertThat(actual).isEqualTo(expected);
    }
}