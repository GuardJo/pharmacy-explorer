package io.github.guardjo.pharmacyexplorer.dto.kakao;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class AddressSearchResponse {
    private MetaDto meta;
    private List<DocumentDto> documents;
}
