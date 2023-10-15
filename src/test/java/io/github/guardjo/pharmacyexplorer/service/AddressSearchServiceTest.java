package io.github.guardjo.pharmacyexplorer.service;

import io.github.guardjo.pharmacyexplorer.AbstractTestContainerTest;
import io.github.guardjo.pharmacyexplorer.config.RetryConfig;
import io.github.guardjo.pharmacyexplorer.config.WebClientConfig;
import io.github.guardjo.pharmacyexplorer.dto.kakao.AddressSearchResponse;
import io.github.guardjo.pharmacyexplorer.util.KakaoUrlMapper;
import io.github.guardjo.pharmacyexplorer.util.TestDataGenerator;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.PropertyNamingStrategy;

import java.io.IOException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ActiveProfiles("test")
class AddressSearchServiceTest {
    @DisplayName("AddressSearchService : Mock Connect Test")
    @Nested
    @SpringBootTest
    @Import({RetryConfig.class, WebClientConfig.class})
    class AddressSearchServiceMockTest extends AbstractTestContainerTest {
        private final MockWebServer mockWebServer = new MockWebServer();

        @MockBean
        private KakaoUrlMapper kakaoUrlMapper;
        private ObjectMapper objectMapper;

        @Autowired
        private AddressSearchService addressSearchService;

        @BeforeEach
        void setUp() throws IOException {
            String testUrlPrefix = "/test-server";
            mockWebServer.start();

            objectMapper = new ObjectMapper();
            objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);

            given(kakaoUrlMapper.searchAddressInfo(anyString())).willReturn(mockWebServer.url(testUrlPrefix).uri());
        }

        @AfterEach
        void tearDown() throws IOException {
            mockWebServer.shutdown();
        }

        @DisplayName("요청 주소에 대한 응답 테스트")
        @Test
        void test_getSearchResponse() throws JsonProcessingException {
            String address = "서울 종로구 종로 201-1";
            AddressSearchResponse expected = TestDataGenerator.addressSearchResponse(address);
            String mockResponseBody = objectMapper.writeValueAsString(expected);
            MockResponse mockResponse = new MockResponse()
                    .setBody(mockResponseBody)
                    .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

            mockWebServer.enqueue(mockResponse);

            AddressSearchResponse actual = addressSearchService.getSearchResponse(address);

            assertThat(actual).isEqualTo(expected);
        }

        @DisplayName("요청 주소에 대한 응답 테스트 : retry")
        @Test
        void test_getSearchResponseRetry() throws JsonProcessingException {
            String address = "서울 종로구 종로 201-1";
            AddressSearchResponse expected = TestDataGenerator.addressSearchResponse(address);
            String mockResponseBody = objectMapper.writeValueAsString(expected);
            MockResponse mockResponse = new MockResponse()
                    .setBody(mockResponseBody)
                    .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

            mockWebServer.enqueue(new MockResponse().setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value()));
            mockWebServer.enqueue(mockResponse);

            AddressSearchResponse actual = addressSearchService.getSearchResponse(address);

            assertThat(actual).isEqualTo(expected);
        }

        @DisplayName("요청 주소에 대한 응답 테스트 : retry all fail")
        @Test
        void test_getSearchResponseRetryAllFail() {
            String address = "서울 종로구 종로 201-1";

            mockWebServer.enqueue(new MockResponse().setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value()));
            mockWebServer.enqueue(new MockResponse().setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value()));

            AddressSearchResponse actual = addressSearchService.getSearchResponse(address);

            assertThat(actual).isEqualTo(AddressSearchResponse.EmptyResponse());
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
        }
    }
}