package io.github.guardjo.pharmacyexplorer.service;

import io.github.guardjo.pharmacyexplorer.domain.Pharmacy;
import io.github.guardjo.pharmacyexplorer.domain.cache.PharmacyCache;
import io.github.guardjo.pharmacyexplorer.repository.cache.PharmacyCacheRepository;
import io.github.guardjo.pharmacyexplorer.util.TestDataGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.mock;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class CacheServiceTest {
    @Mock
    private PharmacyService pharmacyService;
    @Mock
    private PharmacyCacheRepository cacheRepository;

    @InjectMocks
    private CacheService cacheService;

    @DisplayName("캐싱 데이터 저장 테스트")
    @Test
    void test_initCacheData() {
        List<Pharmacy> pharmacies = List.of(TestDataGenerator.pharmacy(1L));

        given(pharmacyService.findAllPharmacies()).willReturn(pharmacies);
        given(cacheRepository.saveAll(anyList())).willReturn(mock(List.class));

        assertThatCode(() -> cacheService.initCacheData()).doesNotThrowAnyException();

        then(pharmacyService).should().findAllPharmacies();
        then(cacheRepository).should().saveAll(anyList());
    }

    @DisplayName("캐싱된 전체 데이터 반환 테스트")
    @Test
    void test_findAllCacheData() {
        List<PharmacyCache> pharmacyCaches = List.of(TestDataGenerator.pharmacyCache(1L));
        List<Pharmacy> expected = pharmacyCaches.stream()
                .map(PharmacyCache::getCacheData)
                .collect(Collectors.toList());
        ;

        given(cacheRepository.findAll()).willReturn(pharmacyCaches);

        List<Pharmacy> actual = cacheService.findAllCacheData();

        assertThat(actual).isEqualTo(expected);

        then(cacheRepository).should().findAll();
    }

    @DisplayName("PharmacyCache 데이터 전체 삭제 테스트")
    @Test
    void test_cleanCache() {
        willDoNothing().given(cacheRepository).deleteAll();

        assertThatCode(() -> cacheService.cleanCache()).doesNotThrowAnyException();

        then(cacheRepository).should().deleteAll();
    }
}