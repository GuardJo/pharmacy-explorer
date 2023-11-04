package io.github.guardjo.pharmacyexplorer.service;

import io.github.guardjo.pharmacyexplorer.domain.ShortenUrl;
import io.github.guardjo.pharmacyexplorer.repository.ShortenUrlRepository;
import io.github.guardjo.pharmacyexplorer.util.ShortenUrlMapper;
import io.github.guardjo.pharmacyexplorer.util.TestDataGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.atLeast;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class ShortenUrlServiceTest {
    @Mock
    private ShortenUrlRepository shortenUrlRepository;
    @Mock
    private ShortenUrlMapper shortenUrlMapper;

    @InjectMocks
    private ShortenUrlService shortenUrlService;

    @DisplayName("인코딩돤 ID 기반으로 원본 URL을 반환")
    @Test
    void test_findOriginalUrl() {
        long originalId = 1L;
        String encodedId = "test";
        ShortenUrl expected = TestDataGenerator.shortenUrl();

        given(shortenUrlMapper.decodeId(eq(encodedId))).willReturn(originalId);
        given(shortenUrlRepository.findById(eq(originalId))).willReturn(Optional.of(expected));

        String actual = shortenUrlService.findOriginalUrl(encodedId);

        assertThat(actual).isEqualTo(expected.getOriginalUrl());

        then(shortenUrlMapper).should().decodeId(eq(encodedId));
        then(shortenUrlRepository).should().findById(eq(originalId));
    }

    @DisplayName("주어진 원본 URL을 단축 URL로 저장")
    @ParameterizedTest
    @MethodSource("shortenUrlParams")
    void test_saveShortenUrl(Optional<ShortenUrl> optionalShortenUrl) {
        String encodedId = "test";
        ShortenUrl shortenUrl = TestDataGenerator.shortenUrl();
        String originalUrl = shortenUrl.getOriginalUrl();

        given(shortenUrlRepository.findByOriginalUrl(eq(originalUrl))).willReturn(optionalShortenUrl);

        if (optionalShortenUrl.isEmpty()) {
            given(shortenUrlRepository.save(any(ShortenUrl.class))).willReturn(shortenUrl);
        }

        given(shortenUrlMapper.encodeId(anyLong())).willReturn(encodedId);

        String actual = shortenUrlService.saveShortenUrl(originalUrl);

        assertThat(actual).isEqualTo(encodedId);

        then(shortenUrlRepository).should().findByOriginalUrl(eq(originalUrl));
        then(shortenUrlRepository).should(atLeast(0)).save(any(ShortenUrl.class));
    }

    public static Stream<Arguments> shortenUrlParams() {
        return Stream.of(
                Arguments.of(Optional.empty()),
                Arguments.of(Optional.of(TestDataGenerator.shortenUrl()))
        );
    }
}