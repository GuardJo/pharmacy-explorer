package io.github.guardjo.pharmacyexplorer.util;

import io.seruco.encoding.base62.Base62;
import org.springframework.stereotype.Component;

@Component
public class ShortenUrlMapper {
    private final Base62 base62 = Base62.createInstance();

    /**
     * 주어진 인자를 인코딩한다.
     *
     * @param originalId 인코딩할 원본 값
     * @return 인코딩된 문자열
     */
    public String encodeId(long originalId) {
        return new String(base62.encode(String.valueOf(originalId).getBytes()));
    }

    /**
     * 주어진 문자열을 디코딩한다.
     *
     * @param encodedId 디코딩할 인코딩된 값
     * @return 디코딩된 원본 값
     */
    public long decodeId(String encodedId) {
        return Long.parseLong(new String(base62.decode(encodedId.getBytes())));
    }
}
