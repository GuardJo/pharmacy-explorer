package io.github.guardjo.pharmacyexplorer.controller;

import io.github.guardjo.pharmacyexplorer.dto.PharmacyDto;
import io.github.guardjo.pharmacyexplorer.dto.kakao.AddressSearchResponse;
import io.github.guardjo.pharmacyexplorer.service.AddressSearchService;
import io.github.guardjo.pharmacyexplorer.service.PharmacySearchService;
import io.github.guardjo.pharmacyexplorer.util.KakaoUrlMapper;
import io.github.guardjo.pharmacyexplorer.util.TestDataGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.net.URI;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(controllers = SearchController.class)
class SearchControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AddressSearchService addressSearchService;
    @MockBean
    private PharmacySearchService pharmacySearchService;
    @MockBean
    private KakaoUrlMapper kakaoUrlMapper;

    @DisplayName("약국 검색 결과 페이지 접근")
    @Test
    void test_searchResult() throws Exception {
        String searchAddress = "test";

        AddressSearchResponse addressSearchResponse = TestDataGenerator.addressSearchResponse(searchAddress);
        PharmacyDto pharmacyDto = PharmacyDto.from(TestDataGenerator.pharmacy());
        given(addressSearchService.getSearchResponse(eq(searchAddress))).willReturn(addressSearchResponse);
        given(pharmacySearchService.searchPharmacies(eq(addressSearchResponse.getDocuments().get(0)))).willReturn(List.of(pharmacyDto));
        given(kakaoUrlMapper.getNavigationUrl(eq(pharmacyDto.getName()), eq(pharmacyDto.getLatitude()),
                eq(pharmacyDto.getLongtitude()))).willReturn(URI.create("#"));
        given(kakaoUrlMapper.getRoadViewUrl(eq(pharmacyDto.getLatitude()), eq(pharmacyDto.getLongtitude()))).willReturn(URI.create("#"));

        mockMvc.perform(get("/search")
                        .queryParam("address", searchAddress))
                .andExpect(status().isOk())
                .andExpect(view().name("search/search-table"))
                .andExpect(model().attributeExists("searchResponses"));

        then(addressSearchService).should().getSearchResponse(eq(searchAddress));
        then(pharmacySearchService).should().searchPharmacies(eq(addressSearchResponse.getDocuments().get(0)));
        then(kakaoUrlMapper).should().getNavigationUrl(eq(pharmacyDto.getName()), eq(pharmacyDto.getLatitude()),
                eq(pharmacyDto.getLongtitude()));
        then(kakaoUrlMapper).should().getRoadViewUrl(eq(pharmacyDto.getLatitude()), eq(pharmacyDto.getLongtitude()));
    }
}