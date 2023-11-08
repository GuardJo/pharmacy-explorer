package io.github.guardjo.pharmacyexplorer.repository.cache;

import io.github.guardjo.pharmacyexplorer.domain.cache.PharmacyCache;
import org.springframework.data.repository.CrudRepository;

public interface PharmacyCacheRepository extends CrudRepository<PharmacyCache, Long> {
}
