package io.github.guardjo.pharmacyexplorer.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(controllers = SearchController.class)
class SearchControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @DisplayName("약국 검색 결과 페이지 접근")
    @Test
    void test_searchResult() throws Exception {
        mockMvc.perform(get("/search"))
                .andExpect(status().isOk())
                .andExpect(view().name("search/search-table"))
                .andExpect(model().attributeExists("searchResponses"));
    }
}