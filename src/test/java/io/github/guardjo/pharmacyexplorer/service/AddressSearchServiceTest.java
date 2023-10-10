package io.github.guardjo.pharmacyexplorer.service;

import io.github.guardjo.pharmacyexplorer.dto.kakao.AddressSearchResponse;
import io.github.guardjo.pharmacyexplorer.util.TestDataGenerator;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URI;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ActiveProfiles("test")
class AddressSearchServiceTest {
    @DisplayName("AddressSearchService : Mock Connect Test")
    @Nested
    @ExtendWith(MockitoExtension.class)
    class AddressSearchServiceMockTest {
        @Mock(answer = Answers.RETURNS_DEEP_STUBS)
        private WebClient kakaoClient;

        @InjectMocks
        private AddressSearchService addressSearchService;

        @DisplayName("요청 주소에 대한 응답 테스트")
        @Test
        void test_getSearchResponse() {
            String address = "서울 종로구 종로 201-1";
            AddressSearchResponse expected = TestDataGenerator.addressSearchResponse(address);
            given(
                    kakaoClient.get()
                            .uri(any(URI.class))
                            .retrieve()
                            .bodyToMono(AddressSearchResponse.class)
                            .block()
            ).willReturn(expected);

            AddressSearchResponse actual = addressSearchService.getSearchResponse(address);

            assertThat(actual).isEqualTo(expected);
        }

        @DisplayName("요청 주소에 대한 응답 테스트 : 주소 미입력")
        @Test
        void test_getSearchResponse_with_null() {
            AddressSearchResponse actual = addressSearchService.getSearchResponse(null);

            assertThat(actual).isNull();
        }
    }

    @Disabled // 일일 요청제한을 혹시 넘을 수 있으므로 default는 disable하도록함
    @DisplayName("AddressSearchService : Real Connect Test")
    @Nested
    @SpringBootTest
    class AddressSearchServiceRealTest {
        @Autowired
        private AddressSearchService addressSearchService;

        @DisplayName("요청 주소에 대한 응답 테스트")
        @Test
        void test_getSearchResponse() {
            String address = "서울 종로구 종로 201-1";

            AddressSearchResponse response = addressSearchService.getSearchResponse(address);

            assertThat(response).isNotNull();
            System.out.println(response);
        }
    }
}