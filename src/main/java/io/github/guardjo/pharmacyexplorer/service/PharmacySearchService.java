package io.github.guardjo.pharmacyexplorer.service;

import io.github.guardjo.pharmacyexplorer.domain.Pharmacy;
import io.github.guardjo.pharmacyexplorer.domain.SearchInfo;
import io.github.guardjo.pharmacyexplorer.dto.PharmacyDto;
import io.github.guardjo.pharmacyexplorer.dto.kakao.DocumentDto;
import io.github.guardjo.pharmacyexplorer.util.DistanceCalculator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class PharmacySearchService {
    private final PharmacyService pharmacyService;
    private final SearchInfoService searchInfoService;
    private final DistanceCalculator distanceCalculator;

    private final static int SEARCH_LIMIT_SIZE = 3;
    private final static int SEARCH_LIMIT_AREA = 10; // 반경 10km

    /**
     * 지정된 위치 기반으로 반경 10km 내 최대 3곳의 약국 정보를 가까운 순으로 반환한다.
     *
     * @param base 지정된 위치
     * @return PharmacyDto List
     */
    public List<PharmacyDto> searchPharmacies(DocumentDto base) {
        log.debug("Searching Pharmacies...");

        if (Objects.isNull(base)) {
            log.warn("Base is Null, Please Enter currect LocaleData");
            return List.of();
        }

        SearchInfo searchInfo = searchInfoService.findSearchInfo(base);

        List<Pharmacy> pharmacies = Objects.isNull(searchInfo) ? pharmacyService.findAllPharmacies() :
                searchInfo.getPharmacies();

        List<PharmacyDto> pharmacyDtoList = pharmacies.stream()
                .map(PharmacyDto::from)
                .map(pharmacyDto -> updateTargetDistance(base, pharmacyDto))
                .filter(pharmacyDto -> pharmacyDto.getTargetDistance() < SEARCH_LIMIT_AREA)
                .sorted(Comparator.comparing(PharmacyDto::getTargetDistance))
                .limit(SEARCH_LIMIT_SIZE)
                .collect(Collectors.toList());

        log.info("Searched Pharmacies. resultSize = {}", pharmacyDtoList.size());

        if (Objects.isNull(searchInfo)) {
            searchInfoService.saveNewSearchInfo(base, pharmacyDtoList.stream()
                    .map(PharmacyDto::toEntity)
                    .collect(Collectors.toList()));
        }

        return pharmacyDtoList;
    }

    private PharmacyDto updateTargetDistance(DocumentDto base, PharmacyDto pharmacyDto) {
        double targetDistance = distanceCalculator.calculateDistanceByHaversine(base.getLatitude(),
                base.getLongitude(), pharmacyDto.getLatitude(), pharmacyDto.getLongtitude());

        pharmacyDto.setTargetDistance(targetDistance);

        return pharmacyDto;
    }
}
