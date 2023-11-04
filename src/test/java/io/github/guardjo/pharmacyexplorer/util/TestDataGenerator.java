package io.github.guardjo.pharmacyexplorer.util;

import io.github.guardjo.pharmacyexplorer.domain.Pharmacy;
import io.github.guardjo.pharmacyexplorer.domain.SearchInfo;
import io.github.guardjo.pharmacyexplorer.domain.ShortenUrl;
import io.github.guardjo.pharmacyexplorer.dto.kakao.AddressSearchResponse;
import io.github.guardjo.pharmacyexplorer.dto.kakao.DocumentDto;
import io.github.guardjo.pharmacyexplorer.dto.kakao.MetaDto;
import org.assertj.core.internal.bytebuddy.utility.RandomString;

import java.util.List;

public class TestDataGenerator {
    public static MetaDto metaDto() {
        return MetaDto.builder()
                .end(true)
                .totalCount(1)
                .pageableCount(1)
                .build();
    }

    public static DocumentDto documentDto(String address) {
        return DocumentDto.builder()
                .addressName(address)
                .addressType("REGION")
                .longitude(100.0)
                .latitude(100.0)
                .build();
    }

    public static AddressSearchResponse addressSearchResponse(String address) {
        return AddressSearchResponse.builder()
                .meta(metaDto())
                .documents(List.of(documentDto(address)))
                .build();
    }

    public static Pharmacy pharmacy() {
        return Pharmacy.builder()
                .name("test")
                .latitude(10.0)
                .longtitude(20.0)
                .address("test address")
                .build();
    }

    public static SearchInfo searchInfo(int lat, int lng) {
        return SearchInfo.builder()
                .baseAddress("test address")
                .baseLat(lat)
                .baseLng(lng)
                .build();
    }

    public static ShortenUrl shortenUrl() {
        return ShortenUrl.builder()
                .id(1L)
                .originalUrl(RandomString.make(30))
                .build();
    }
}
