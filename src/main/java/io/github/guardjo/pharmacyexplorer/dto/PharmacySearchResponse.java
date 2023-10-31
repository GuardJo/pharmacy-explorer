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

    public static PharmacySearchResponse from(PharmacyDto pharmacyDto, String mapUrl, String roadViewUrl) {
        return new PharmacySearchResponse(
                pharmacyDto.getName(),
                pharmacyDto.getAddress(),
                mapUrl,
                roadViewUrl,
                (float) pharmacyDto.getTargetDistance()
        );
    }
}
