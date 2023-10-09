package io.github.guardjo.pharmacyexplorer.util;

import io.github.guardjo.pharmacyexplorer.dto.kakao.AddressSearchResponse;
import io.github.guardjo.pharmacyexplorer.dto.kakao.DocumentDto;
import io.github.guardjo.pharmacyexplorer.dto.kakao.MetaDto;

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
                .latitude(100.0)
                .longitude(100.0)
                .build();
    }

    public static AddressSearchResponse addressSearchResponse(String address) {
        return AddressSearchResponse.builder()
                .meta(metaDto())
                .documents(List.of(documentDto(address)))
                .build();
    }
}
