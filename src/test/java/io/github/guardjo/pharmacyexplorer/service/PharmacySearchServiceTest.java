package io.github.guardjo.pharmacyexplorer.service;

import io.github.guardjo.pharmacyexplorer.domain.Pharmacy;
import io.github.guardjo.pharmacyexplorer.dto.PharmacyDto;
import io.github.guardjo.pharmacyexplorer.dto.kakao.DocumentDto;
import io.github.guardjo.pharmacyexplorer.util.DistanceCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class PharmacySearchServiceTest {
    @Mock
    private PharmacyService pharmacyService;
    @Mock
    private DistanceCalculator distanceCalculator;

    @InjectMocks
    private PharmacySearchService pharmacySearchService;

    private final List<Pharmacy> testData = new ArrayList<>();

    @BeforeEach
    void setUp() {
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

        testData.add(song);
        testData.add(jinsung);
        testData.add(pangyo);

        given(pharmacyService.findAllPharmacies()).willReturn(testData);
    }

    @DisplayName("주어진 위치에서 가까운 순으로 최대 3개의 약국 정보 반환")
    @Test
    void test_searchPharmacies() {
        // 의정부역 기준
        double baseLat = 37.738415;
        double baseLng = 127.045958;

        DocumentDto base = DocumentDto.builder()
                .addressName("의정부역")
                .latitude(baseLat)
                .longitude(baseLng)
                .build();

        given(distanceCalculator.calculateDistanceByHaversine(eq(baseLat), eq(baseLng), anyDouble(), anyDouble())).willCallRealMethod();
        List<PharmacyDto> actual = pharmacySearchService.searchPharmacies(base);

        assertThat(actual).isNotNull();
        assertThat(actual.size()).isEqualTo(testData.size() - 1); // 판교 약국은 반경 10km 외라 제외됨
        assertThat(actual.get(0).getName()).isEqualTo("진성약국");
        assertThat(actual.get(1).getName()).isEqualTo("송약국");
    }
}