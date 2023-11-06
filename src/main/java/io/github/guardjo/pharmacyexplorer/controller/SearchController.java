package io.github.guardjo.pharmacyexplorer.controller;

import io.github.guardjo.pharmacyexplorer.constants.ContextPath;
import io.github.guardjo.pharmacyexplorer.dto.PharmacyDto;
import io.github.guardjo.pharmacyexplorer.dto.PharmacySearchResponse;
import io.github.guardjo.pharmacyexplorer.dto.kakao.AddressSearchResponse;
import io.github.guardjo.pharmacyexplorer.dto.kakao.DocumentDto;
import io.github.guardjo.pharmacyexplorer.service.AddressSearchService;
import io.github.guardjo.pharmacyexplorer.service.PharmacySearchService;
import io.github.guardjo.pharmacyexplorer.service.ShortenUrlService;
import io.github.guardjo.pharmacyexplorer.util.KakaoUrlMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping(ContextPath.SEARCH_URL_ENDPOINT)
@Slf4j
@RequiredArgsConstructor
public class SearchController {
    private final AddressSearchService addressSearchService;
    private final PharmacySearchService pharmacySearchService;
    private final ShortenUrlService shortenUrlService;
    private final KakaoUrlMapper kakaoUrlMapper;

    @GetMapping
    public String searchResult(ModelMap modelMap, @RequestParam("address") String address, HttpServletRequest req) {
        log.info("GET /search, address = {}", address);
        String scheme = req.getScheme();
        String server = req.getServerName();
        int port = req.getServerPort();

        String contextPath = scheme + "://" + server + ":" + port;

        List<PharmacySearchResponse> searchResponses = new ArrayList<>();

        AddressSearchResponse addressSearchResponse = addressSearchService.getSearchResponse(address);

        if (addressSearchResponse.getDocuments().isEmpty()) {
            log.warn("Not Found Address");
        } else {
            DocumentDto documentDto = addressSearchResponse.getDocuments().get(0);

            List<PharmacyDto> pharmacyDtoList = pharmacySearchService.searchPharmacies(documentDto);

            searchResponses.addAll(pharmacyDtoList.stream()
                    .map(pharmacyDto -> {
                        String naviUrl = kakaoUrlMapper.getNavigationUrl(pharmacyDto.getName(),
                                pharmacyDto.getLatitude(), pharmacyDto.getLongtitude()).toString();
                        String roadViewUrl = kakaoUrlMapper.getRoadViewUrl(pharmacyDto.getLatitude(),
                                pharmacyDto.getLongtitude()).toString();

                        String naviEncodedUrl =
                                contextPath + ContextPath.SHORTEN_URL_ENDPOINT + "/" + shortenUrlService.saveShortenUrl(naviUrl);
                        String roadViewEncodedUrl =
                                contextPath + ContextPath.SHORTEN_URL_ENDPOINT + "/" + shortenUrlService.saveShortenUrl(roadViewUrl);

                        return PharmacySearchResponse.from(pharmacyDto, naviEncodedUrl, roadViewEncodedUrl);
                    })
                    .collect(Collectors.toList()));
        }

        modelMap.addAttribute("searchResponses", searchResponses);

        return "search/search-table";
    }
}
