package io.github.guardjo.pharmacyexplorer.util;

import io.github.guardjo.pharmacyexplorer.constants.ExternalUrls;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Component
public class KakaoUrlMapper {
    /**
     * 주어진 주소에 대해 Kakao 로컬 API 내 주소 검색 URI를 반환한다
     * <hr>
     * <a href="https://developers.kakao.com/docs/latest/ko/local/dev-guide">카카오 로컬 API</a>
     *
     * @param address
     * @return 카카오 로컬 API (주소 검색) URI
     */
    public URI searchAddressInfo(String address) {
        return UriComponentsBuilder.fromUriString(ExternalUrls.KAKAO_SEARCH_ADDRESS_URL)
                .queryParam("query", address)
                .build()
                .encode()
                .toUri();
    }
}
