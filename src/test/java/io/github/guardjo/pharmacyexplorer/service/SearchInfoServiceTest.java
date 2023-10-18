package io.github.guardjo.pharmacyexplorer.service;

import io.github.guardjo.pharmacyexplorer.domain.Pharmacy;
import io.github.guardjo.pharmacyexplorer.domain.SearchInfo;
import io.github.guardjo.pharmacyexplorer.dto.kakao.DocumentDto;
import io.github.guardjo.pharmacyexplorer.repository.SearchInfoRepository;
import io.github.guardjo.pharmacyexplorer.util.TestDataGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class SearchInfoServiceTest {
    @Mock
    private SearchInfoRepository searchInfoRepository;

    @InjectMocks
    private SearchInfoService searchInfoService;

    @DisplayName("DocumentDto에 해당하는 SearchInfo 조회")
    @ParameterizedTest
    @MethodSource("searchInfoTestParams")
    void test_findSearchInfo(DocumentDto documentDto, Optional<SearchInfo> searchInfo) {
        given(searchInfoRepository.findByBaseLatAndBaseLng(eq(documentDto.getLatitude()),
                eq(documentDto.getLongitude()))).willReturn(searchInfo);

        SearchInfo actual = searchInfoService.findSearchInfo(documentDto);

        if (searchInfo.isPresent()) {
            assertThat(actual).isEqualTo(searchInfo.get());
        } else {
            assertThat(actual).isNull();
        }

        then(searchInfoRepository).should().findByBaseLatAndBaseLng(eq(documentDto.getLatitude()),
                eq(documentDto.getLongitude()));
    }

    @DisplayName("주어진 documentDto, PharmacyList를 기반으로 SearchInfo 신규 저장")
    @Test
    void test_saveNewSearchInfo() {
        DocumentDto documentDto = TestDataGenerator.documentDto("test address");
        List<Pharmacy> pharmacyList = List.of(TestDataGenerator.pharmacy());

        ArgumentCaptor<SearchInfo> saveSearchInfoCaptor = ArgumentCaptor.forClass(SearchInfo.class);
        given(searchInfoRepository.save(saveSearchInfoCaptor.capture())).willReturn(any(SearchInfo.class));

        assertThatCode(() -> searchInfoService.saveNewSearchInfo(documentDto, pharmacyList)).doesNotThrowAnyException();

        SearchInfo actual = saveSearchInfoCaptor.getValue();

        assertThat(actual).isNotNull();
        assertThat(actual.getBaseLat()).isEqualTo(documentDto.getLatitude());
        assertThat(actual.getBaseLng()).isEqualTo(documentDto.getLongitude());
        assertThat(actual.getPharmacies().size()).isEqualTo(pharmacyList.size());
    }

    static Stream<Arguments> searchInfoTestParams() {
        DocumentDto documentDto = TestDataGenerator.documentDto("test address");
        return Stream.of(
                Arguments.of(documentDto, Optional.of(TestDataGenerator.searchInfo((int) documentDto.getLatitude(),
                        (int) documentDto.getLongitude()))),
                Arguments.of(documentDto, Optional.empty())
        );
    }
}