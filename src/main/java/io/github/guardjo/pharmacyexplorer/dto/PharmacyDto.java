package io.github.guardjo.pharmacyexplorer.dto;

import io.github.guardjo.pharmacyexplorer.domain.Pharmacy;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class PharmacyDto {
    private Long id;
    private String address;
    private String name;
    private double longtitude;
    private double latitude;
    @Setter
    private double targetDistance;

    public static PharmacyDto from(Pharmacy pharmacy) {
        return new PharmacyDto(
                pharmacy.getId(),
                pharmacy.getAddress(),
                pharmacy.getName(),
                pharmacy.getLongtitude(),
                pharmacy.getLatitude(),
                0.0
        );
    }

    public static Pharmacy toEntity(PharmacyDto pharmacyDto) {
        return Pharmacy.builder()
                .id(pharmacyDto.getId())
                .address(pharmacyDto.getAddress())
                .name(pharmacyDto.getName())
                .longtitude(pharmacyDto.getLongtitude())
                .latitude(pharmacyDto.getLatitude())
                .build();
    }
}
