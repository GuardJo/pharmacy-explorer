package io.github.guardjo.pharmacyexplorer.repository;

import io.github.guardjo.pharmacyexplorer.domain.ShortenUrl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShortenUrlRepository extends JpaRepository<ShortenUrl, Long> {
    Optional<ShortenUrl> findByOriginalUrl(String originalUrl);
}
