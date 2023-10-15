package io.github.guardjo.pharmacyexplorer.service;

import io.github.guardjo.pharmacyexplorer.domain.Pharmacy;
import io.github.guardjo.pharmacyexplorer.domain.SearchInfo;
import io.github.guardjo.pharmacyexplorer.dto.kakao.DocumentDto;
import io.github.guardjo.pharmacyexplorer.repository.SearchInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class SearchInfoService {
    private final SearchInfoRepository searchInfoRepository;

    /**
     * 현재 주어진 요청 주소 데이터에 대한 SearchInfo를 반환한다.
     *
     * @param documentDto 사용자가 입력한 주소 기반 정보
     * @return 입력 위/경도에 해당하는 SearchInfo 값 반환 (없을 경우 null)
     */
    public SearchInfo findSearchInfo(DocumentDto documentDto) {
        double baseLat = documentDto.getLatitude();
        double baseLng = documentDto.getLongitude();

        log.debug("Searching searchInfo... , lat : {}, lng : {}", baseLat, baseLng);

        Optional<SearchInfo> searchInfo = searchInfoRepository.findByBaseLatAndBaseLng(baseLat, baseLng);

        if (searchInfo.isPresent()) {
            log.info("Searched searchInfo");
            return searchInfo.get();
        } else {
            return null;
        }
    }

    /**
     * 사용자가 입력한 주소 기반 정보와 그에 해당하는 약국 검색 결과 목록을 저장한다.
     *
     * @param documentDto 사용자 입력 주소 기반 정보
     * @param pharmacies  검색된 약국 정보 Entitiy
     * @return 신규 저장된 SearchInfo
     */
    public SearchInfo saveNewSearchInfo(DocumentDto documentDto, List<Pharmacy> pharmacies) {
        log.debug("Initialize New SearchInfo, baseAddress = {}, baseLat = {}, baseLng = {}",
                documentDto.getAddressName(), documentDto.getLatitude(), documentDto.getLongitude());

        SearchInfo searchInfo = SearchInfo.builder()
                .baseAddress(documentDto.getAddressName())
                .baseLat(documentDto.getLatitude())
                .baseLng(documentDto.getLongitude())
                .build();

        searchInfo.getPharmacies().addAll(pharmacies);

        log.info("Save new SearchInfo, baseAddress = {}, baseLat = {}, baseLng = {}, pharmacySize = {}",
                searchInfo.getBaseAddress(), searchInfo.getBaseLat(), searchInfo.getBaseLng(),
                searchInfo.getPharmacies().size());

        return searchInfoRepository.save(searchInfo);
    }
}
