package io.github.guardjo.pharmacyexplorer.service;

import io.github.guardjo.pharmacyexplorer.dto.kakao.AddressSearchResponse;
import io.github.guardjo.pharmacyexplorer.util.KakaoUrlMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URI;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class AddressSearchService {
    private final WebClient kakaoClient;
    private final KakaoUrlMapper kakaoUrlMapper;

    /**
     * 주소(도로명/지번) 입력 시 그에 해당하는 응답 값 (위도, 경도 등)을 반환한다.
     *
     * @param address 조회할 주소(도로명 or 지번)
     * @return 해당 주소에 대한 위도, 경도를 포함한 위치 데이터
     */
    @Retryable(
            value = Exception.class,
            maxAttempts = 2,
            backoff = @Backoff(value = 3000)
    )
    public AddressSearchResponse getSearchResponse(String address) {
        log.info("Searching Address : {}", address);

        if (Objects.isNull(address)) {
            log.warn("Bad Request : Address is Null");
            return null;
        }

        URI requestUri = kakaoUrlMapper.searchAddressInfo(address);

        AddressSearchResponse response = kakaoClient.get()
                .uri(requestUri)
                .retrieve()
                .bodyToMono(AddressSearchResponse.class)
                .block();

        log.info("Searched Address, total = {}", response.getMeta().getTotalCount());

        return response;
    }

    @Recover
    public AddressSearchResponse recoverGetSearchResponse(Exception e, String address) {
        log.error("Request and all retry is Failed, address = {}", address, e);

        return AddressSearchResponse.EmptyResponse();
    }
}
