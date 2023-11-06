package io.github.guardjo.pharmacyexplorer.controller;

import io.github.guardjo.pharmacyexplorer.constants.ContextPath;
import io.github.guardjo.pharmacyexplorer.service.ShortenUrlService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(ContextPath.SHORTEN_URL_ENDPOINT)
@Controller
@Slf4j
@RequiredArgsConstructor
public class ShortenUrlController {
    private final ShortenUrlService shortenUrlService;

    @GetMapping("/{encodedId}")
    public String redirectOriginalUrl(@PathVariable("encodedId") String encodedId) {
        log.info("Redirect Original Url, encodedId = {}", encodedId);

        return "redirect:" + shortenUrlService.findOriginalUrl(encodedId);
    }
}
