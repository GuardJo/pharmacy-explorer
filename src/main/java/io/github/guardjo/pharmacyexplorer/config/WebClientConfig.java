package io.github.guardjo.pharmacyexplorer.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    @Value("${kakao.api-key}")
    private String kakaoApiKey;

    @Bean
    public WebClient kakaoClient() {
        return WebClient.builder()
                .defaultHeader(HttpHeaders.AUTHORIZATION, "KakaoAK " + kakaoApiKey)
                .build();
    }
}
