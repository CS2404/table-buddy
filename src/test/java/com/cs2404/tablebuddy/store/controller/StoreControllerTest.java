package com.cs2404.tablebuddy.store.controller;


import com.cs2404.tablebuddy.TestSecurityConfig;
import com.cs2404.tablebuddy.store.dto.StoreDto;
import com.cs2404.tablebuddy.store.service.StoreService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
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

    @Test
    void 가게_다건_조회_API_호출_성공() throws Exception {
        // given
        int page = 0;
        int size = 10;

        StoreDto storeDto1 = StoreDto.builder()
                .id(1L)
                .name("김밥천국")
                .build();

        StoreDto storeDto2 = StoreDto.builder()
                .id(2L)
                .name("분식천국")
                .build();

        List<StoreDto> storeList = List.of(
                storeDto1, storeDto2
        );
        Page<StoreDto> storePage = new PageImpl<>(storeList, PageRequest.of(page, size), storeList.size());

        when(storeService.getStores(page, size)).thenReturn(storePage);

        // when & then
        mockMvc.perform(get("/api/v1/stores")
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(storeList.size()))
                .andExpect(jsonPath("$.content[0].name").value("김밥천국"))
                .andExpect(jsonPath("$.content[1].name").value("분식천국"));
    }
}
