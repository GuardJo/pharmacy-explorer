package io.github.guardjo.pharmacyexplorer.controller;

import io.github.guardjo.pharmacyexplorer.dto.PharmacyDto;
import io.github.guardjo.pharmacyexplorer.dto.PharmacySearchResponse;
import io.github.guardjo.pharmacyexplorer.dto.kakao.AddressSearchResponse;
import io.github.guardjo.pharmacyexplorer.dto.kakao.DocumentDto;
import io.github.guardjo.pharmacyexplorer.service.AddressSearchService;
import io.github.guardjo.pharmacyexplorer.service.PharmacySearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/search")
@Slf4j
@RequiredArgsConstructor
public class SearchController {
    private final AddressSearchService addressSearchService;
    private final PharmacySearchService pharmacySearchService;

    @GetMapping
    public String searchResult(ModelMap modelMap, @RequestParam("address") String address) {
        log.info("GET /search, address = {}", address);
        List<PharmacySearchResponse> searchResponses = new ArrayList<>();

        AddressSearchResponse addressSearchResponse = addressSearchService.getSearchResponse(address);

        if (addressSearchResponse.getDocuments().isEmpty()) {
            log.warn("Not Found Address");
        } else {
            DocumentDto documentDto = addressSearchResponse.getDocuments().get(0);

            List<PharmacyDto> pharmacyDtoList = pharmacySearchService.searchPharmacies(documentDto);

            searchResponses.addAll(pharmacyDtoList.stream()
                    .map(PharmacySearchResponse::from)
                    .collect(Collectors.toList()));
        }

        modelMap.addAttribute("searchResponses", searchResponses);

        return "search/search-table";
    }
}
