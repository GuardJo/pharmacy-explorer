package io.github.guardjo.pharmacyexplorer.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class ShortenUrlMapperTest {
    private ShortenUrlMapper shortenUrlMapper;

    @BeforeEach
    void setUp() {
        this.shortenUrlMapper = new ShortenUrlMapper();
    }

    @DisplayName("단축 URL 암/복호화")
    @Test
    void test_encodeAndDecode() {
        long originalId = 19960220;
        String encodedId = shortenUrlMapper.encodeId(originalId);
        long decodedId = shortenUrlMapper.decodeId(encodedId);

        assertThat(decodedId).isEqualTo(originalId);
    }
}