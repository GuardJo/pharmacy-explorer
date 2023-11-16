package io.github.guardjo.pharmacyexplorer.controller.rest;

import io.github.guardjo.pharmacyexplorer.service.CacheService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class CacheController {
    private final CacheService cacheService;

    @GetMapping("/cache")
    public String initCacheData() {
        log.info("Initializing PharmacyCacheData...");

        cacheService.cleanCache();
        cacheService.initCacheData();

        log.info("Initialized PharmacyCacheData");
        return "SUCCESSES";
    }
}
