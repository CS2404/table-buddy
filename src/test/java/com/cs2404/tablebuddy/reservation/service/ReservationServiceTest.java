package com.cs2404.tablebuddy.reservation.service;

import com.cs2404.tablebuddy.TestUtils;
import com.cs2404.tablebuddy.common.exception.CustomBusinessException;
import com.cs2404.tablebuddy.member.dto.MemberDto;
import com.cs2404.tablebuddy.common.entity.DeleteStatus;
import com.cs2404.tablebuddy.member.entity.MemberEntity;
import com.cs2404.tablebuddy.member.entity.MemberRole;
import com.cs2404.tablebuddy.member.repository.MemberRepository;
import com.cs2404.tablebuddy.reservation.dto.ReservationDto;
import com.cs2404.tablebuddy.reservation.entity.ReservationEntity;
import com.cs2404.tablebuddy.reservation.entity.ReservationStatus;
import com.cs2404.tablebuddy.reservation.repository.ReservationRepository;
import com.cs2404.tablebuddy.store.entity.StoreEntity;
import com.cs2404.tablebuddy.store.repository.StoreRepository;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @InjectMocks
    ReservationService reservationService;

    @Mock
    MemberRepository memberRepository;
    @Mock
    ReservationRepository reservationRepository;
    @Mock
    StoreRepository storeRepository;

    private static final MemberDto customerMemberDto = MemberDto.builder()
            .id(1L)
            .role(MemberRole.CUSTOMER)
            .build();

    private static final MemberDto anotherCustomerMemberDto = MemberDto.builder()
            .id(2L)
            .role(MemberRole.CUSTOMER)
            .build();

    private static final MemberDto ownerMemberDto = MemberDto.builder()
            .id(2L)
            .role(MemberRole.OWNER)
            .build();

    private MemberEntity getCustomerMemberEntity() {
        EasyRandomParameters customerParam = new EasyRandomParameters();
        customerParam.randomize(MemberRole.class, () -> MemberRole.CUSTOMER);
        EasyRandom customerRandom = new EasyRandom(customerParam);
        return customerRandom.nextObject(MemberEntity.class);
    }

    private MemberEntity getOwnerMemberEntity() {
        EasyRandomParameters customerParam = new EasyRandomParameters();
        customerParam.randomize(MemberRole.class, () -> MemberRole.OWNER);
        EasyRandom customerRandom = new EasyRandom(customerParam);
        return customerRandom.nextObject(MemberEntity.class);
    }

    private StoreEntity getStoreEntity() {
        EasyRandomParameters customerParam = new EasyRandomParameters();
        customerParam.randomize(DeleteStatus.class, () -> DeleteStatus.N);
        EasyRandom customerRandom = new EasyRandom(customerParam);
        return customerRandom.nextObject(StoreEntity.class);
    }

    private ReservationEntity getReservationEntity() {
        EasyRandomParameters customerParam = new EasyRandomParameters();
        customerParam.randomize(ReservationStatus.class, () -> ReservationStatus.PENDING);
        customerParam.randomize(DeleteStatus.class, () -> DeleteStatus.N);

        EasyRandom customerRandom = new EasyRandom(customerParam);

        return customerRandom.nextObject(ReservationEntity.class);
    }

    @Test
    @DisplayName("예약 추가 성공 테스트")
    void addReservationTest_Success() {
        // given
        MemberEntity memberEntity = getCustomerMemberEntity();
        StoreEntity storeEntity = getStoreEntity();
        int peopleCount = 4;

        when(memberRepository.findMemberByMemberId(customerMemberDto.getId()))
                .thenReturn(Optional.of(memberEntity));

        when(storeRepository.findById(storeEntity.getId()))
                .thenReturn(Optional.of(storeEntity));

        when(reservationRepository.saveReservation(any(ReservationEntity.class)))
                .thenReturn(1L); // 저장된 예약 ID 반환

        // when
        reservationService.addReservation(
                customerMemberDto,
                storeEntity.getId(),
                ReservationStatus.PENDING,
                peopleCount
        );

        // then
        verify(reservationRepository).saveReservation(any(ReservationEntity.class));
    }

    @Test
    @DisplayName("존재하지 않는 회원으로 예약 추가 시 예외 발생")
    void addReservationTest_MemberNotFound() {
        // given
        MemberEntity memberEntity = getCustomerMemberEntity();
        StoreEntity storeEntity = getStoreEntity();
        int peopleCount = 4;

        when(memberRepository.findMemberByMemberId(customerMemberDto.getId()))
                .thenReturn(Optional.empty()); // 회원 없음

        // when & then
        assertThrows(CustomBusinessException.class, () ->
                reservationService.addReservation(customerMemberDto, storeEntity.getId(), ReservationStatus.PENDING, peopleCount));

        verify(memberRepository).findMemberByMemberId(customerMemberDto.getId());
    }

    @Test
    @DisplayName("존재하지 않는 가게로 예약 추가 시 예외 발생")
    void addReservationTest_StoreNotFound() {
        // given
        MemberEntity memberEntity = getCustomerMemberEntity();
        StoreEntity storeEntity = getStoreEntity();
        int peopleCount = 4;

        when(memberRepository.findMemberByMemberId(customerMemberDto.getId()))
                .thenReturn(Optional.of(memberEntity));

        when(storeRepository.findById(storeEntity.getId()))
                .thenReturn(Optional.empty()); // 가게 없음

        // when & then
        assertThrows(CustomBusinessException.class, () ->
                reservationService.addReservation(customerMemberDto, storeEntity.getId(), ReservationStatus.PENDING, peopleCount));

        verify(storeRepository).findById(storeEntity.getId());
    }

    @Test
    @DisplayName("예약 삭제 성공")
    void deleteReservationTest_Success() {
        // given
        MemberEntity memberEntity = getCustomerMemberEntity();
        StoreEntity storeEntity = getStoreEntity();
        int peopleCount = 4;
        Long reservationId = 1L;

        ReservationEntity reservationEntity = ReservationEntity.builder()
                .id(reservationId)
                .reservationStatus(ReservationStatus.PENDING)
                .peopleCount(peopleCount)
                .isDeleted(DeleteStatus.N)
                .memberEntity(memberEntity)
                .storeId(storeEntity.getId())
                .build();

        when(memberRepository.findMemberByMemberId(customerMemberDto.getId()))
                .thenReturn(Optional.of(memberEntity));

        when(reservationRepository.findReservation(reservationId))
                .thenReturn(Optional.of(reservationEntity));

        // when
        Long deletedReservationId = reservationService.deleteReservation(customerMemberDto, reservationId);

        // then
        verify(reservationRepository).findReservation(reservationId);

        assertEquals(reservationId, deletedReservationId);
        assertEquals(ReservationStatus.REJECTED, reservationEntity.getReservationStatus());
        assertEquals(DeleteStatus.Y, reservationEntity.getIsDeleted());
    }

    @Test
    @DisplayName("삭제 실패: 예약 상태가 PENDING이 아닐 경우")
    void deleteReservationTest_ReservationNotPending() {
        // given
        MemberEntity memberEntity = getCustomerMemberEntity();
        StoreEntity storeEntity = getStoreEntity();
        int peopleCount = 4;

        ReservationEntity reservationEntity = ReservationEntity.builder()
                .id(1L)
                .reservationStatus(ReservationStatus.CONFIRMED)  // 이미 승인됨
                .peopleCount(peopleCount)
                .isDeleted(DeleteStatus.N)
                .memberEntity(memberEntity)
                .storeId(storeEntity.getId())
                .build();

        when(memberRepository.findMemberByMemberId(customerMemberDto.getId()))
                .thenReturn(Optional.of(memberEntity));

        when(reservationRepository.findReservation(reservationEntity.getId()))
                .thenReturn(Optional.of(reservationEntity));

        // when & then
        assertThrows(CustomBusinessException.class, () ->
                reservationService.deleteReservation(customerMemberDto, reservationEntity.getId()));

        verify(reservationRepository).findReservation(reservationEntity.getId());
    }

    @Test
    @DisplayName("삭제 실패: 예약자가 아닌 다른 사용자가 삭제 시")
    void deleteReservationTest_UnauthorizedUser() {
        // given
        MemberEntity memberEntity = getCustomerMemberEntity();
        MemberEntity anotherMemberEntity = getCustomerMemberEntity();
        StoreEntity storeEntity = getStoreEntity();
        int peopleCount = 4;

        ReservationEntity reservationEntity = ReservationEntity.builder()
                .id(1L)
                .reservationStatus(ReservationStatus.CONFIRMED)
                .peopleCount(peopleCount)
                .isDeleted(DeleteStatus.N)
                .memberEntity(memberEntity)
                .storeId(storeEntity.getId())
                .build();

        when(memberRepository.findMemberByMemberId(anotherCustomerMemberDto.getId()))
                .thenReturn(Optional.of(anotherMemberEntity));
        when(reservationRepository.findReservation(reservationEntity.getId()))
                .thenReturn(Optional.of(reservationEntity));

        // when & then
        assertThrows(CustomBusinessException.class, () ->
                reservationService.deleteReservation(anotherCustomerMemberDto, reservationEntity.getId()));

        verify(reservationRepository).findReservation(reservationEntity.getId());
    }

    @Test
    @DisplayName("예약 수정 성공")
    void editReservationTest_Success() {
        // given
        MemberEntity memberEntity = getCustomerMemberEntity();
        StoreEntity storeEntity = getStoreEntity();
        Long reservationId = 1L;
        int peopleCount = 4;
        int newPeopleCount = 5;

        ReservationEntity reservationEntity = ReservationEntity.builder()
                .id(reservationId)
                .reservationStatus(ReservationStatus.PENDING)
                .peopleCount(peopleCount)
                .isDeleted(DeleteStatus.N)
                .memberEntity(memberEntity)
                .storeId(storeEntity.getId())
                .build();

        when(memberRepository.findMemberByMemberId(customerMemberDto.getId()))
                .thenReturn(Optional.of(memberEntity));
        when(reservationRepository.findReservation(reservationId))
                .thenReturn(Optional.of(reservationEntity));

        // when
        Long editedReservationId = reservationService.editReservation(
                customerMemberDto,
                reservationId,
                newPeopleCount
        );

        // then
        verify(reservationRepository).findReservation(reservationId);

        assertEquals(reservationId, editedReservationId);
        assertEquals(newPeopleCount, reservationEntity.getPeopleCount());
    }

    @Test
    @DisplayName("사장이 예약 승인 성공")
    void Reservation_approved_Success() {
        // given
        MemberEntity memberEntity = getCustomerMemberEntity();
        StoreEntity storeEntity = getStoreEntity();
        Long reservationId = 1L;
        int peopleCount = 4;

        ReservationEntity reservationEntity = ReservationEntity.builder()
                .id(reservationId)
                .reservationStatus(ReservationStatus.PENDING)
                .peopleCount(peopleCount)
                .isDeleted(DeleteStatus.N)
                .memberEntity(memberEntity)
                .storeId(storeEntity.getId())
                .build();

        when(memberRepository.findMemberByMemberId(customerMemberDto.getId()))
                .thenReturn(Optional.of(memberEntity));
        when(reservationRepository.findReservation(reservationId))
                .thenReturn(Optional.of(reservationEntity));

        // when
        Long editedReservationId = reservationService.approveReservation(
                customerMemberDto,
                reservationId
        );

        // then
        verify(reservationRepository).findReservation(reservationId);

        assertEquals(reservationId, editedReservationId);
        assertEquals(ReservationStatus.CONFIRMED, reservationEntity.getReservationStatus());
    }

    @Test
    @DisplayName("줄서기 조회 성공: 예약한 고객")
    void Reservation_find_by_customer() {
        // given
        MemberEntity memberEntity = getCustomerMemberEntity();
        StoreEntity storeEntity = getStoreEntity();
        Long reservationId = 1L;
        int peopleCount = 4;

        ReservationEntity reservationEntity = ReservationEntity.builder()
                .id(reservationId)
                .reservationStatus(ReservationStatus.PENDING)
                .peopleCount(peopleCount)
                .isDeleted(DeleteStatus.N)
                .memberEntity(memberEntity)
                .storeId(storeEntity.getId())
                .build();

        when(memberRepository.findMemberByMemberId(customerMemberDto.getId()))
                .thenReturn(Optional.of(memberEntity));
        when(reservationRepository.findReservation(reservationId))
                .thenReturn(Optional.of(reservationEntity));
        when(storeRepository.findById(storeEntity.getId()))
                .thenReturn(Optional.of(storeEntity));

        // when
        ReservationDto reservationDto = reservationService.findReservation(
                customerMemberDto,
                reservationId
        );

        // then
        verify(memberRepository).findMemberByMemberId(customerMemberDto.getId());
        verify(storeRepository).findById(reservationEntity.getStoreId());
        verify(reservationRepository).findReservation(reservationId);

        assertEquals(reservationDto.getId(), reservationEntity.getId());
        assertEquals(reservationDto.getReservationStatus(), reservationEntity.getReservationStatus());
        assertEquals(reservationDto.getStoreId(), reservationEntity.getStoreId());
        assertEquals(reservationDto.getPeopleCount(), reservationEntity.getPeopleCount());
    }

    @Test
    @DisplayName("줄서기 조회 성공: 가게 주인")
    void Reservation_find_by_owner() {
        // given
        MemberEntity memberEntity = getOwnerMemberEntity();
        MemberEntity ownerMemberEntity = getOwnerMemberEntity();
        StoreEntity storeEntity = StoreEntity.builder()
                .id(1L)
                .name("가게")
                .member(ownerMemberEntity)
                .build();
        Long reservationId = 1L;
        int peopleCount = 4;

        ReservationEntity reservationEntity = ReservationEntity.builder()
                .id(reservationId)
                .reservationStatus(ReservationStatus.PENDING)
                .peopleCount(peopleCount)
                .isDeleted(DeleteStatus.N)
                .memberEntity(memberEntity)
                .storeId(storeEntity.getId())
                .build();

        when(memberRepository.findMemberByMemberId(ownerMemberDto.getId()))
                .thenReturn(Optional.of(memberEntity));
        when(reservationRepository.findReservation(reservationId))
                .thenReturn(Optional.of(reservationEntity));
        when(storeRepository.findById(storeEntity.getId()))
                .thenReturn(Optional.of(storeEntity));

        // when
        ReservationDto reservationDto = reservationService.findReservation(
                ownerMemberDto,
                reservationId
        );

        // then
        verify(memberRepository).findMemberByMemberId(ownerMemberDto.getId());
        verify(storeRepository).findById(reservationEntity.getStoreId());
        verify(reservationRepository).findReservation(reservationId);

        assertEquals(reservationDto.getId(), reservationEntity.getId());
        assertEquals(reservationDto.getReservationStatus(), reservationEntity.getReservationStatus());
        assertEquals(reservationDto.getStoreId(), reservationEntity.getStoreId());
        assertEquals(reservationDto.getPeopleCount(), reservationEntity.getPeopleCount());
    }

    @Test
    @DisplayName("줄서기 대기순번 조회 성공: 0번째")
    void Reservation__waitingOrder_0_find() {
        // given
        MemberEntity memberEntity = getCustomerMemberEntity();
        StoreEntity storeEntity = getStoreEntity();
        Long reservationId = 1L;
        int peopleCount = 4;

        ReservationEntity reservationEntity = ReservationEntity.builder()
                .id(reservationId)
                .reservationStatus(ReservationStatus.PENDING)
                .peopleCount(peopleCount)
                .isDeleted(DeleteStatus.N)
                .memberEntity(memberEntity)
                .storeId(storeEntity.getId())
                .build();

        List<ReservationEntity> reservationEntityList = new ArrayList<>();
        reservationEntityList.add(reservationEntity);

        when(memberRepository.findMemberByMemberId(customerMemberDto.getId()))
                .thenReturn(Optional.of(memberEntity));
        when(reservationRepository.findReservation(reservationId))
                .thenReturn(Optional.of(reservationEntity));
        when(reservationRepository.findReservationList(storeEntity.getId()))
                .thenReturn(reservationEntityList);

        // when
        Long waitingOrder = reservationService.selectWaitingOrder(
                customerMemberDto,
                reservationId
        );

        // then
        verify(memberRepository).findMemberByMemberId(customerMemberDto.getId());
        verify(reservationRepository).findReservation(reservationId);

        assertEquals(0L, waitingOrder);
    }

    @Test
    @DisplayName("줄서기 대기순번 조회 성공: 1번쨰")
    void Reservation__waitingOrder_1_find() {
        // given
        MemberEntity memberEntity = getCustomerMemberEntity();
        MemberEntity anotherMemberEntity = getCustomerMemberEntity();
        StoreEntity storeEntity = getStoreEntity();
        Long reservationId = 2L;
        int peopleCount = 4;

        ReservationEntity anotherReservationEntity = ReservationEntity.builder()
                .id(1L)
                .reservationStatus(ReservationStatus.PENDING)
                .peopleCount(peopleCount)
                .isDeleted(DeleteStatus.N)
                .memberEntity(anotherMemberEntity)
                .storeId(storeEntity.getId())
                .build();
        TestUtils.setCreatedAt(anotherReservationEntity, LocalDateTime.now().minusHours(1));  // 강제 설정

        ReservationEntity reservationEntity = ReservationEntity.builder()
                .id(2L)
                .reservationStatus(ReservationStatus.PENDING)
                .peopleCount(peopleCount)
                .isDeleted(DeleteStatus.N)
                .memberEntity(memberEntity)
                .storeId(storeEntity.getId())
                .build();
        TestUtils.setCreatedAt(reservationEntity, LocalDateTime.now());  // 강제 설정


        List<ReservationEntity> reservationEntityList = new ArrayList<>();
        reservationEntityList.add(anotherReservationEntity);
        reservationEntityList.add(reservationEntity);

        when(memberRepository.findMemberByMemberId(customerMemberDto.getId()))
                .thenReturn(Optional.of(memberEntity));
        when(reservationRepository.findReservation(reservationId))
                .thenReturn(Optional.of(reservationEntity));
        when(reservationRepository.findReservationList(storeEntity.getId()))
                .thenReturn(reservationEntityList);

        // when
        Long waitingOrder = reservationService.selectWaitingOrder(
                customerMemberDto,
                reservationId
        );

        // then
        verify(memberRepository).findMemberByMemberId(customerMemberDto.getId());
        verify(reservationRepository).findReservation(reservationId);

        assertEquals(1L, waitingOrder);
    }


}