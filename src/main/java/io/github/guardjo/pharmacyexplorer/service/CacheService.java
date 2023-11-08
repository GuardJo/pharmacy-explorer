package io.github.guardjo.pharmacyexplorer.service;

import io.github.guardjo.pharmacyexplorer.domain.Pharmacy;
import io.github.guardjo.pharmacyexplorer.domain.cache.PharmacyCache;
import io.github.guardjo.pharmacyexplorer.repository.cache.PharmacyCacheRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Slf4j
@RequiredArgsConstructor
public class CacheService {
    private final PharmacyCacheRepository cacheRepository;
    private final PharmacyService pharmacyService;

    /**
     * DB 에 저장된 약국 데이터들을 캐싱한다.
     */
    public void initCacheData() {
        log.info("Caching Pharmacy Data...");

        List<Pharmacy> pharmacies = pharmacyService.findAllPharmacies();

        cacheRepository.saveAll((Iterable) pharmacies);

        log.info("Cached Successes");
    }

    /**
     * 캐싱된 약국 데이터 전제 반환
     *
     * @return 캐싱된 약국 데이터 목록
     */
    public List<Pharmacy> findAllCacheData() {
        log.info("FindAll PharmacyCaches");

        List<PharmacyCache> caches = StreamSupport.stream(cacheRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());

        return caches.stream()
                .map(PharmacyCache::getCacheData)
                .collect(Collectors.toList());
    }

    private PharmacyCache convertCacheData(Pharmacy pharmacy) {
        return PharmacyCache.builder()
                .id(pharmacy.getId())
                .cacheData(pharmacy)
                .build();
    }
}
