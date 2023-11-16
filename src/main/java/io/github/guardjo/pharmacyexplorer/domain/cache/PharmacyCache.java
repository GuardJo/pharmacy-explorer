package io.github.guardjo.pharmacyexplorer.domain.cache;

import io.github.guardjo.pharmacyexplorer.domain.Pharmacy;
import lombok.*;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.Id;

@RedisHash("CACHE_PHARMACY")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class PharmacyCache {
    @Id
    private Long id;
    private Pharmacy cacheData;
}
