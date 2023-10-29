package io.github.guardjo.pharmacyexplorer.controller;

import io.github.guardjo.pharmacyexplorer.dto.PharmacySearchResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/search")
@Slf4j
public class SearchController {
    @GetMapping
    public String searchResult(ModelMap modelMap) {
        log.info("GET /search");

        List<PharmacySearchResponse> searchResponses = List.of(
                PharmacySearchResponse.builder()
                        .pharmacyName("약국1")
                        .address("약국로 1길")
                        .mapUrl("#")
                        .roadViewUrl("#")
                        .distance(1f)
                        .build(),
                PharmacySearchResponse.builder()
                        .pharmacyName("약국2")
                        .address("약국로 2길")
                        .mapUrl("#")
                        .roadViewUrl("#")
                        .distance(1f)
                        .build(),
                PharmacySearchResponse.builder()
                        .pharmacyName("약국3")
                        .address("약국로 3길")
                        .mapUrl("#")
                        .roadViewUrl("#")
                        .distance(1f)
                        .build()
        );

        modelMap.addAttribute("searchResponses", searchResponses);

        return "search/search-table";
    }
}
