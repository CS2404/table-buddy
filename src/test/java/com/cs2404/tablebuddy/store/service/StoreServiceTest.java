package com.cs2404.tablebuddy.store.service;

import com.cs2404.tablebuddy.common.exception.CustomBusinessException;
import com.cs2404.tablebuddy.common.exception.ErrorCode;
import com.cs2404.tablebuddy.member.dto.MemberDto;
import com.cs2404.tablebuddy.common.entity.DeleteStatus;
import com.cs2404.tablebuddy.member.entity.MemberEntity;
import com.cs2404.tablebuddy.member.entity.MemberRole;
import com.cs2404.tablebuddy.member.repository.MemberRepository;
import com.cs2404.tablebuddy.store.dto.SaveBusinessHourDto;
import com.cs2404.tablebuddy.store.dto.SaveStoreDto;
import com.cs2404.tablebuddy.store.entity.BusinessDay;
import com.cs2404.tablebuddy.store.entity.BusinessHourEntity;
import com.cs2404.tablebuddy.store.entity.Category;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StoreServiceTest {

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private MemberRepository memberRepository;

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

    @Test
    void 가게_등록_성공() {
        // given
        MemberEntity member = MemberEntity.builder()
                .id(1L)
                .name("jihyun")
                .isDeleted(DeleteStatus.N)
                .role(MemberRole.OWNER)
                .phoneNumber("010-1111-1111")
                .email("abc@gmail.com")
                .build();

        SaveStoreDto.Request request = SaveStoreDto.Request.builder()
                .name("김밥천국")
                .category(Category.KOREAN)
                .maxWaitingCapacity(30L)
                .build();

        MemberDto memberDto = new MemberDto(member);
        // when
        when(memberRepository.findMemberByMemberId(anyLong())).thenReturn(
                Optional.of(member));

        when(storeRepository.findByMember(member)).thenReturn(Optional.empty());

        storeService.saveStore(request, memberDto);

        // Then
        verify(storeRepository, times(1)).saveStore(any(StoreEntity.class));

    }

    @Test
    void 가게_등록_권한_실패() {
        // given
        MemberEntity member = MemberEntity.builder()
                .id(1L)
                .name("jihyun")
                .isDeleted(DeleteStatus.N)
                .role(MemberRole.CUSTOMER)
                .phoneNumber("010-1111-1111")
                .email("abc@gmail.com")
                .build();

        SaveStoreDto.Request request = SaveStoreDto.Request.builder()
                .name("김밥천국")
                .category(Category.KOREAN)
                .maxWaitingCapacity(30L)
                .build();

        MemberDto memberDto = new MemberDto(member);

        // when
        when(memberRepository.findMemberByMemberId(anyLong())).thenReturn(
                Optional.of(member));

        // Then
        CustomBusinessException exception = assertThrows(CustomBusinessException.class, () -> {
            storeService.saveStore(request, memberDto);
        });

        assertEquals(ErrorCode.INVALID_ROLE, exception.getErrorCode());
    }

    @Test
    void 가게_추가_등록_실패() {
        // given
        MemberEntity member = MemberEntity.builder()
                .id(1L)
                .name("jihyun")
                .isDeleted(DeleteStatus.N)
                .role(MemberRole.OWNER)
                .phoneNumber("010-1111-1111")
                .email("abc@gmail.com")
                .build();

        SaveStoreDto.Request request = SaveStoreDto.Request.builder()
                .name("김밥천국")
                .category(Category.KOREAN)
                .maxWaitingCapacity(30L)
                .build();

        MemberDto memberDto = new MemberDto(member);

        StoreEntity store = StoreEntity.builder()
                .id(1L)
                .name("분식천국")
                .category(Category.KOREAN)
                .maxWaitingCapacity(20L).build();

        // when
        when(memberRepository.findMemberByMemberId(anyLong())).thenReturn(
                Optional.of(member));

        when(storeRepository.findByMember(member)).thenReturn(Optional.of(store));

        // Then
        CustomBusinessException exception = assertThrows(CustomBusinessException.class, () -> {
            storeService.saveStore(request, memberDto);
        });

        assertEquals(ErrorCode.ALREADY_EXIST_STORE, exception.getErrorCode());
    }
}