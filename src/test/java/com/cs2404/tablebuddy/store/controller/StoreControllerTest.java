package com.cs2404.tablebuddy.store.controller;


import com.cs2404.tablebuddy.TestSecurityConfig;
import com.cs2404.tablebuddy.store.dto.StoreDto;
import com.cs2404.tablebuddy.store.service.StoreService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(TestSecurityConfig.class)
@WebMvcTest(controllers = StoreController.class)
class StoreControllerTest {

    @MockBean
    private StoreService storeService;

    @Autowired
    protected MockMvc mockMvc;

    @Test
    void 가게_조회_API_호출_성공() throws Exception {
        // Given
        Long storeId = 1L;
        StoreDto storeDto = StoreDto.builder()
                .id(storeId)
                .name("김밥천국")
                .build();

        given(storeService.getStore(storeId)).willReturn(storeDto);

        // When & Then
        mockMvc.perform(get("/api/v1/stores/" + storeId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(storeId))
                .andExpect(jsonPath("$.name").value(storeDto.getName()));
    }
}
