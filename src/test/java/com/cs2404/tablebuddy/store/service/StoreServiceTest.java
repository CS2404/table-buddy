package com.cs2404.tablebuddy.store.service;

import com.cs2404.tablebuddy.common.exception.CustomBusinessException;
import com.cs2404.tablebuddy.common.exception.ErrorCode;
import com.cs2404.tablebuddy.store.dto.SaveBusinessHourDto;
import com.cs2404.tablebuddy.store.entity.BusinessDay;
import com.cs2404.tablebuddy.store.entity.BusinessHourEntity;
import com.cs2404.tablebuddy.store.entity.StoreEntity;
import com.cs2404.tablebuddy.store.repository.StoreRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StoreServiceTest {

    @Mock
    private StoreRepository storeRepository;

    @InjectMocks
    private StoreService storeService;

    @Test
    void 가게_영업시간_등록_성공() {
        // Given
        StoreEntity store = StoreEntity.builder()
                .id(1L)
                .build();

        SaveBusinessHourDto.Request request = SaveBusinessHourDto.Request.builder()
                .startTime(LocalTime.of(9, 0))
                .endTime(LocalTime.of(18, 0))
                .openDays(List.of("월요일", "수요일"))
                .build();

        when(storeRepository.findById(1L)).thenReturn(Optional.of(store));

        // When
        storeService.saveBusinessHour(1L, request);

        // Then
        verify(storeRepository, times(2)).saveBusinessHour(any(BusinessHourEntity.class));
    }

    @Test
    void 잘못된_영업요일_예외_처리() {
        // Given
        String invalidDay = "슈요일";

        // When & Then
        CustomBusinessException exception = assertThrows(CustomBusinessException.class, () -> {
            BusinessDay.fromString(invalidDay);
        });

        assertEquals(ErrorCode.INVALID_BUSINESS_DAY, exception.getErrorCode());
        assertEquals("잘못된 영업요일을 입력하셨습니다. 입력된 값: 슈요일", exception.getMessage());
    }

}