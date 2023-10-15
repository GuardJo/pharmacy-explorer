package io.github.guardjo.pharmacyexplorer.dto.kakao;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.List;

@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class AddressSearchResponse {
    private MetaDto meta;
    private List<DocumentDto> documents;

    public static AddressSearchResponse EmptyResponse() {
        return new AddressSearchResponse(MetaDto.emptyMeta(), List.of());
    }
}
