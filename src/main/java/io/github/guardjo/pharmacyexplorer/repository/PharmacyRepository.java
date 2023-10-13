package io.github.guardjo.pharmacyexplorer.repository;

import io.github.guardjo.pharmacyexplorer.domain.Pharmacy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PharmacyRepository extends JpaRepository<Pharmacy, Long> {
}
