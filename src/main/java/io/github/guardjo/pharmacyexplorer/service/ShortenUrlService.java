package io.github.guardjo.pharmacyexplorer.service;

import io.github.guardjo.pharmacyexplorer.domain.ShortenUrl;
import io.github.guardjo.pharmacyexplorer.repository.ShortenUrlRepository;
import io.github.guardjo.pharmacyexplorer.util.ShortenUrlMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ShortenUrlService {
    private final ShortenUrlRepository shortenUrlRepository;
    private final ShortenUrlMapper shortenUrlMapper;

    /**
     * 주어진 encodedId(단축 URL)에 해당하는 원본 URL을 반환한다.
     *
     * @param encodedId 인코딩된 ShortenUrl 식별키
     * @return 원본 URL
     */
    public String findOriginalUrl(String encodedId) {
        log.info("Find OriginalUrl, encodedId = {}", encodedId);
        long originalId = shortenUrlMapper.decodeId(encodedId);

        Optional<ShortenUrl> shortenUrl = shortenUrlRepository.findById(originalId);

        if (shortenUrl.isEmpty()) {
            log.warn("Not Found Shorten Url");
            return "#";
        }

        return shortenUrl.get().getOriginalUrl();
    }

    /**
     * 주어진 원본 URL 저장 및 인코딩된 단축 URL을 반환한다.
     *
     * @param originalUrl 단축 URL 정보로 반환할 원본 URL
     * @return 단축 URL
     */
    public String saveShortenUrl(String originalUrl) {
        log.info("Save ShortenUrl, originalUrl = {}", originalUrl);
        long originalId = 0L;

        Optional<ShortenUrl> shortenUrl = shortenUrlRepository.findByOriginalUrl(originalUrl);

        if (shortenUrl.isPresent()) {
            log.info("Already Saved Data, originalUrl = {}", originalUrl);
            originalId = shortenUrl.get().getId();
        } else {
            ShortenUrl newShortenUrl = shortenUrlRepository.save(ShortenUrl.builder()
                    .originalUrl(originalUrl)
                    .build());

            originalId = newShortenUrl.getId();
        }

        return shortenUrlMapper.encodeId(originalId);
    }
}
