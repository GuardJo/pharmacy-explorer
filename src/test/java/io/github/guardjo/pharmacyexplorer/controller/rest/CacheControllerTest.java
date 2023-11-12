package io.github.guardjo.pharmacyexplorer.controller.rest;

import io.github.guardjo.pharmacyexplorer.service.CacheService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CacheController.class)
class CacheControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CacheService cacheService;

    @DisplayName("GET : /cache")
    @Test
    void test_initCacheData() throws Exception {
        String expected = "SUCCESSES";

        willDoNothing().given(cacheService).cleanCache();
        willDoNothing().given(cacheService).initCacheData();

        String actual = mockMvc.perform(get("/cache"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        assertThat(actual).isEqualTo(expected);

        then(cacheService).should().cleanCache();
        then(cacheService).should().initCacheData();
    }
}