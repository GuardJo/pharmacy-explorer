package io.github.guardjo.pharmacyexplorer.service;

import io.github.guardjo.pharmacyexplorer.domain.Pharmacy;
import io.github.guardjo.pharmacyexplorer.domain.SearchInfo;
import io.github.guardjo.pharmacyexplorer.dto.PharmacyDto;
import io.github.guardjo.pharmacyexplorer.dto.kakao.DocumentDto;
import io.github.guardjo.pharmacyexplorer.util.DistanceCalculator;
import io.github.guardjo.pharmacyexplorer.util.TestDataGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.atLeast;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class PharmacySearchServiceTest {
    @Mock
    private PharmacyService pharmacyService;
    @Mock
    private SearchInfoService searchInfoService;
    @Mock
    private CacheService cacheService;
    @Mock
    private DistanceCalculator distanceCalculator;

    @InjectMocks
    private PharmacySearchService pharmacySearchService;

    @DisplayName("주어진 위치에서 가까운 순으로 최대 3개의 약국 정보 반환")
    @ParameterizedTest
    @MethodSource("paramPharmacies")
    void test_searchPharmacies(List<Pharmacy> pharmacies, SearchInfo searchInfo, boolean isCaching) {
        // 의정부역 기준
        double baseLat = 37.738415;
        double baseLng = 127.045958;

        DocumentDto base = DocumentDto.builder()
                .addressName("의정부역")
                .latitude(baseLat)
                .longitude(baseLng)
                .build();

        given(searchInfoService.findSearchInfo(eq(base))).willReturn(searchInfo);
        if (searchInfo == null) {
            if (isCaching) {
                given(cacheService.findAllCacheData()).willReturn(pharmacies);
            } else {
                given(cacheService.findAllCacheData()).willReturn(List.of());
                given(pharmacyService.findAllPharmacies()).willReturn(pharmacies);
            }
            given(searchInfoService.saveNewSearchInfo(eq(base), anyList())).willReturn(TestDataGenerator.searchInfo(1, 1));
        }
        given(distanceCalculator.calculateDistanceByHaversine(eq(baseLat), eq(baseLng), anyDouble(), anyDouble())).willCallRealMethod();

        List<PharmacyDto> actual = pharmacySearchService.searchPharmacies(base);

        assertThat(actual).isNotNull();
        assertThat(actual.get(0).getName()).isEqualTo("진성약국");
        assertThat(actual.get(1).getName()).isEqualTo("송약국");

        then(searchInfoService).should().findSearchInfo(eq(base));
        then(cacheService).should(atLeast(0)).findAllCacheData();
        then(pharmacyService).should(atLeast(0)).findAllPharmacies();
        then(searchInfoService).should(atLeast(0)).saveNewSearchInfo(eq(base), anyList());
        then(distanceCalculator).should(atLeast(1)).calculateDistanceByHaversine(eq(baseLat), eq(baseLng), anyDouble(),
                anyDouble());
    }

    public static Stream<Arguments> paramPharmacies() {
        Pharmacy song = Pharmacy.builder()
                .name("송약국")
                .latitude(37.74152616)
                .longtitude(127.0495112)
                .address("경기도 의정부시 호국로1310번길 46, 1층 (의정부동)")
                .build();

        Pharmacy jinsung = Pharmacy.builder()
                .name("진성약국")
                .latitude(37.74026154)
                .longtitude(127.0475922)
                .address("경기도 의정부시 평화로 542 (의정부동)")
                .build();

        Pharmacy pangyo = Pharmacy.builder()
                .name("판교파미어스약국")
                .latitude(37.4134038262846)
                .longtitude(127.098334109561)
                .address("경기 성남시 수정구 창업로 18")
                .build();

        List<Pharmacy> testData = List.of(song, jinsung, pangyo);
        SearchInfo searchInfo = SearchInfo.builder()
                .pharmacies(testData)
                .build();

        return Stream.of(
                Arguments.of(testData, null, false),
                Arguments.of(testData, null, true),
                Arguments.of(null, searchInfo, false)
        );
    }
}