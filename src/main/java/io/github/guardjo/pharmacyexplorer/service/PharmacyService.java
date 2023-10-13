package io.github.guardjo.pharmacyexplorer.service;

import io.github.guardjo.pharmacyexplorer.domain.Pharmacy;
import io.github.guardjo.pharmacyexplorer.repository.PharmacyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PharmacyService {
    private final PharmacyRepository pharmacyRepository;

    /**
     * DB에 저장된 전체 약국 데이터를 반환한다.
     *
     * @return Pharmacy List
     */
    @Transactional(readOnly = true)
    public List<Pharmacy> findAllPharmacies() {
        log.info("Find all Pharmacies");

        return pharmacyRepository.findAll();
    }
}
