package io.github.guardjo.pharmacyexplorer.controller;


import io.github.guardjo.pharmacyexplorer.constants.ContextPath;
import io.github.guardjo.pharmacyexplorer.service.ShortenUrlService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ShortenUrlController.class)
class ShortenUrlControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ShortenUrlService shortenUrlService;

    @DisplayName("단축 URL에 대한 원본 URL 접근 테스트")
    @Test
    void test_redirectOriginalUrl() throws Exception {
        String orginalUrl = "http://localhost";
        String encodedId = "test";

        given(shortenUrlService.findOriginalUrl(eq(encodedId))).willReturn(orginalUrl);

        mockMvc.perform(get(ContextPath.SHORTEN_URL_ENDPOINT + "/" + encodedId))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(orginalUrl));

        then(shortenUrlService).should().findOriginalUrl(eq(encodedId));
    }
}