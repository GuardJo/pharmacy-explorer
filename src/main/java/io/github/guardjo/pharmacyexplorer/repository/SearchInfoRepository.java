package io.github.guardjo.pharmacyexplorer.repository;

import io.github.guardjo.pharmacyexplorer.domain.SearchInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SearchInfoRepository extends JpaRepository<SearchInfo, Long> {
    Optional<SearchInfo> findByBaseLatAndBaseLng(double baseLat, double baseLng);
}
