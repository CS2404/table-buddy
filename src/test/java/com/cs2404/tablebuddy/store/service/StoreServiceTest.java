package com.cs2404.tablebuddy.store.service;

import com.cs2404.tablebuddy.common.exception.CustomBusinessException;
import com.cs2404.tablebuddy.common.exception.ErrorCode;
import com.cs2404.tablebuddy.member.dto.MemberDto;
import com.cs2404.tablebuddy.member.entity.DeleteStatus;
import com.cs2404.tablebuddy.member.entity.MemberEntity;
import com.cs2404.tablebuddy.member.entity.MemberRole;
import com.cs2404.tablebuddy.member.repository.MemberRepository;
import com.cs2404.tablebuddy.store.dto.SaveBusinessHourDto;
import com.cs2404.tablebuddy.store.dto.SaveStoreDto;
import com.cs2404.tablebuddy.store.dto.StoreDto;
import com.cs2404.tablebuddy.store.dto.UpdateStoreDto;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
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

    @Test
    void 가게_수정_성공() {

        // given
        Long storeId = 1L;
        Long memberId = 1L;

        MemberEntity member = MemberEntity.builder()
                .id(memberId)
                .name("jihyun")
                .isDeleted(DeleteStatus.N)
                .role(MemberRole.OWNER)
                .phoneNumber("010-1111-1111")
                .email("abc@gmail.com")
                .build();

        StoreEntity store = StoreEntity.builder()
                .id(storeId)
                .member(member)
                .name("분식천국")
                .category(Category.KOREAN)
                .maxWaitingCapacity(20L)
                .build();

        UpdateStoreDto.Request request = UpdateStoreDto.Request.builder()
                .name("김밥천국")
                .category(Category.KOREAN)
                .maxWaitingCapacity(30L)
                .build();

        // Mocking
        when(storeRepository.findById(storeId)).thenReturn(Optional.of(store));
        when(memberRepository.findMemberByMemberId(member.getId())).thenReturn(Optional.of(member));

        // when
        Long updatedStoreId = storeService.updateStore(request, new MemberDto(member), storeId);

        // then
        assertAll(
                () -> assertEquals(storeId, updatedStoreId),
                () -> assertEquals("김밥천국", store.getName()),
                () -> assertEquals(Category.KOREAN, store.getCategory()),
                () -> assertEquals(30L, store.getMaxWaitingCapacity())
        );

        verify(storeRepository, times(1)).findById(storeId);

    }

    @Test
    void 가게_조회_성공() {

        // given
        Long storeId = 1L;
        Long memberId = 1L;

        MemberEntity member = MemberEntity.builder()
                .id(memberId)
                .name("jihyun")
                .isDeleted(DeleteStatus.N)
                .role(MemberRole.OWNER)
                .phoneNumber("010-1111-1111")
                .email("abc@gmail.com")
                .build();

        StoreEntity store = StoreEntity.builder()
                .id(storeId)
                .member(member)
                .name("분식천국")
                .category(Category.KOREAN)
                .maxWaitingCapacity(20L)
                .build();


        // Mocking
        when(storeRepository.findById(storeId)).thenReturn(Optional.of(store));

        // when
        StoreDto storeDto = storeService.getStore(storeId);

        // then
        assertAll(
                () -> assertEquals(store.getId(), storeDto.getId()),
                () -> assertEquals(store.getName(), storeDto.getName()),
                () -> assertEquals(store.getCategory(), storeDto.getCategory()),
                () -> assertEquals(store.getMaxWaitingCapacity(), storeDto.getMaxWaitingCapacity())
        );

        verify(storeRepository, times(1)).findById(storeId);

    }

    @Test
    void 가게_다건_조회_성공() {

        // given
        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);

        MemberEntity member1 = MemberEntity.builder()
                .id(1L)
                .build();

        MemberEntity member2 = MemberEntity.builder()
                .id(2L)
                .build();

        StoreEntity store1 = StoreEntity.builder()
                .id(1L)
                .member(member1)
                .name("김밥천국")
                .category(Category.KOREAN)
                .maxWaitingCapacity(20L)
                .build();

        StoreEntity store2 = StoreEntity.builder()
                .id(2L)
                .member(member2)
                .name("분식천국")
                .category(Category.KOREAN)
                .maxWaitingCapacity(20L)
                .build();

        List<StoreEntity> storeList = List.of(
                store1, store2
        );

        long totalCount = storeList.size();  // QueryDSL에서 count 쿼리 실행한 결과

        Page<StoreEntity> storePage = new PageImpl<>(storeList, pageable, totalCount);

        when(storeRepository.findAll(pageable)).thenReturn(storePage);

        // when
        Page<StoreDto> result = storeService.getStores(page, size);

        // then
        assertEquals(storeList.size(), result.getContent().size());
        assertEquals("김밥천국", result.getContent().get(0).getName());
        assertEquals("분식천국", result.getContent().get(1).getName());

        verify(storeRepository, times(1)).findAll(pageable);

    }
}