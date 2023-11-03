package io.github.guardjo.pharmacyexplorer.util;

import io.github.guardjo.pharmacyexplorer.constants.ExternalUrls;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Component
public class KakaoUrlMapper {
    private final String KAKAO_MAP_ENDPOINT_DELIMITER = ",";

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

    /**
     * 주어진 장소명, 위도, 경도를 기반으로 카카오 지도 URL을 생성한다.
     *
     * @param placeName  장소명
     * @param latitude   위도
     * @param longtitude 경도
     * @return 해당하는 장소의 카카오 길찾기 URL
     */
    public URI getNavigationUrl(String placeName, double latitude, double longtitude) {
        String endpoint = String.join(KAKAO_MAP_ENDPOINT_DELIMITER, placeName, String.valueOf(latitude),
                String.valueOf(longtitude));

        return UriComponentsBuilder.fromHttpUrl(ExternalUrls.KAKAO_MAP_NAVI_URL + endpoint)
                .build()
                .encode()
                .toUri();
    }

    /**
     * 주어진 위도, 경도를 기반으로 카카오 지도 로드뷰 URL읇 반환한다.
     *
     * @param latitude   위도
     * @param longtitude 경도
     * @return 해당하는 위치의 카카오 로드뷰 URL
     */
    public URI getRoadViewUrl(double latitude, double longtitude) {
        String endpoint = String.join(KAKAO_MAP_ENDPOINT_DELIMITER, String.valueOf(latitude),
                String.valueOf(longtitude));

        return UriComponentsBuilder.fromHttpUrl(ExternalUrls.KAKAO_MAP_ROADVIEW_URL + endpoint)
                .build()
                .encode()
                .toUri();
    }
}
