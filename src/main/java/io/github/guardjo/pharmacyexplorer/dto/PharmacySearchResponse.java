package io.github.guardjo.pharmacyexplorer.dto;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class PharmacySearchResponse {
    private String pharmacyName;
    private String address;
    private String mapUrl;
    private String roadViewUrl;
    private float distance;

    public static PharmacySearchResponse from(PharmacyDto pharmacyDto) {
        return new PharmacySearchResponse(
                pharmacyDto.getName(),
                pharmacyDto.getAddress(),
                null, // TODO 거리뷰 및 길찾기 URL은 추후 서비스 연동 예정
                null,
                (float) pharmacyDto.getTargetDistance()
        );
    }
}
