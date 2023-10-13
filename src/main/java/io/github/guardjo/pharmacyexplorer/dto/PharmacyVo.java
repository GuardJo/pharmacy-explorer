package io.github.guardjo.pharmacyexplorer.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.guardjo.pharmacyexplorer.domain.Pharmacy;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class PharmacyVo {
    @JsonProperty("도로명전체주소")
    private String address;
    @JsonProperty("사업장명")
    private String name;
    @JsonProperty("경도")
    private double longtitude;
    @JsonProperty("위도")
    private double latitude;

    public static Pharmacy toEntity(PharmacyVo vo) {
        return Pharmacy.builder()
                .name(vo.name)
                .address(vo.address)
                .longtitude(vo.longtitude)
                .latitude(vo.latitude)
                .build();
    }
}
