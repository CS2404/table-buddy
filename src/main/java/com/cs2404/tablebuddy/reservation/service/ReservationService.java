package com.cs2404.tablebuddy.reservation.service;

import com.cs2404.tablebuddy.common.exception.CustomBusinessException;
import com.cs2404.tablebuddy.common.exception.ErrorCode;
import com.cs2404.tablebuddy.member.dto.MemberDto;
import com.cs2404.tablebuddy.member.entity.MemberEntity;
import com.cs2404.tablebuddy.member.repository.MemberRepository;
import com.cs2404.tablebuddy.reservation.dto.ReservationDto;
import com.cs2404.tablebuddy.common.entity.DeleteStatus;
import com.cs2404.tablebuddy.reservation.entity.ReservationEntity;
import com.cs2404.tablebuddy.reservation.entity.ReservationStatus;
import com.cs2404.tablebuddy.reservation.repository.ReservationRepository;
import com.cs2404.tablebuddy.store.entity.StoreEntity;
import com.cs2404.tablebuddy.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReservationService {

    private final MemberRepository memberRepository;
    private final ReservationRepository reservationRepository;
    private final StoreRepository storeRepository;

    @Transactional
    public Long addReservation(MemberDto memberDto,
                               Long storeId,
                               ReservationStatus reservationStatus,
                               int peopleCount
    ) {

        // 회원 조회
        MemberEntity memberEntity = memberRepository.findMemberByMemberId(memberDto.getId())
                .orElseThrow(() -> new CustomBusinessException(ErrorCode.MEMBER_NOT_FOUND));

        // 가게 조회
        StoreEntity storeEntity = storeRepository.findById(storeId)
                .orElseThrow(() -> new CustomBusinessException(ErrorCode.STORE_NOT_FOUND));

        ReservationEntity newReservation = ReservationEntity.builder()
                .reservationStatus(reservationStatus)
                .peopleCount(peopleCount)
                .isDeleted(DeleteStatus.N)
                .memberEntity(memberEntity)
                .storeId(storeId)
                .build();

        return reservationRepository.saveReservation(newReservation);
    }

    @Transactional
    public Long deleteReservation(MemberDto memberDto,
                                  Long reservationId
    ) {

        // 회원 조회
        MemberEntity memberEntity = memberRepository.findMemberByMemberId(memberDto.getId())
                .orElseThrow(() -> new CustomBusinessException(ErrorCode.MEMBER_NOT_FOUND));

        // 예약 조회
        ReservationEntity reservationEntity = reservationRepository.findReservation(reservationId)
                .orElseThrow(() -> new CustomBusinessException(ErrorCode.RESERVATION_NOT_FOUND));

        // 대기 상태에서만
        if (reservationEntity.getReservationStatus() != ReservationStatus.PENDING) {
            throw new CustomBusinessException(ErrorCode.RESERVATION_PERMISSION_ERROR);
        }

        if (!memberEntity.getId().equals(reservationEntity.getMemberEntity().getId())) {
            throw new CustomBusinessException(ErrorCode.RESERVATION_PERMISSION_ERROR);
        }

        // 예약 삭제(상태변경)
        reservationEntity.cancelReservation();

        return reservationId;
    }

    @Transactional
    public Long editReservation(MemberDto memberDto,
                                Long reservationId,
                                int peopleCount
    ) {
        // 회원 조회
        MemberEntity memberEntity = memberRepository.findMemberByMemberId(memberDto.getId())
                .orElseThrow(() -> new CustomBusinessException(ErrorCode.MEMBER_NOT_FOUND));

        // 예약 조회
        ReservationEntity reservationEntity = reservationRepository.findReservation(reservationId)
                .orElseThrow(() -> new CustomBusinessException(ErrorCode.RESERVATION_NOT_FOUND));

        // 대기 상태에서만
        if (reservationEntity.getReservationStatus() != ReservationStatus.PENDING) {
            throw new CustomBusinessException(ErrorCode.RESERVATION_PERMISSION_ERROR);
        }

        if (!memberEntity.getId().equals(reservationEntity.getMemberEntity().getId())) {
            throw new CustomBusinessException(ErrorCode.RESERVATION_PERMISSION_ERROR);
        }

        if (reservationEntity.getPeopleCount() != peopleCount) {
            reservationEntity.changePeopleCount(peopleCount);
        }

        return reservationId;
    }

    @Transactional
    public Long approveReservation(MemberDto memberDto,
                                   Long reservationId
    ) {

        // 회원 조회
        MemberEntity memberEntity = memberRepository.findMemberByMemberId(memberDto.getId())
                .orElseThrow(() -> new CustomBusinessException(ErrorCode.MEMBER_NOT_FOUND));

        // 예약 조회
        ReservationEntity reservationEntity = reservationRepository.findReservation(reservationId)
                .orElseThrow(() -> new CustomBusinessException(ErrorCode.RESERVATION_NOT_FOUND));

        // 아직 승인되지 않은 예약을 대상으로
        if (reservationEntity.getReservationStatus() != ReservationStatus.PENDING) {
            throw new CustomBusinessException(ErrorCode.RESERVATION_PERMISSION_ERROR);
        }

        // 본인 소유의 가게
        if (!memberEntity.getId().equals(reservationEntity.getStoreId())) {
            throw new CustomBusinessException(ErrorCode.RESERVATION_PERMISSION_ERROR);
        }

        // 예약 승인
        reservationEntity.changeReservationStatus(ReservationStatus.CONFIRMED);

        return reservationId;
    }

    public ReservationDto findReservation(MemberDto memberDto,
                                          Long reservationId
    ) {

        // 회원 조회
        MemberEntity memberEntity = memberRepository.findMemberByMemberId(memberDto.getId())
                .orElseThrow(() -> new CustomBusinessException(ErrorCode.MEMBER_NOT_FOUND));

        // 예약 조회
        ReservationEntity reservationEntity = reservationRepository.findReservation(reservationId)
                .orElseThrow(() -> new CustomBusinessException(ErrorCode.RESERVATION_NOT_FOUND));

        // 가게 조회
        StoreEntity storeEntity = storeRepository.findById(reservationEntity.getStoreId())
                .orElseThrow(() -> new CustomBusinessException(ErrorCode.STORE_NOT_FOUND));

        if (memberEntity.isCustomer()
                && !memberEntity.getId().equals(reservationEntity.getMemberEntity().getId())
        ) {
            // 고객인 경우, 본인 예약 정보만 조회 가능
            throw new CustomBusinessException(ErrorCode.RESERVATION_PERMISSION_ERROR);
        }

        if (memberEntity.isOwner()
                && !memberEntity.getId().equals(storeEntity.getMember().getId())
        ) {
            // 사장인 경우, 본인 가게 예약 정보만 조회 가능
            throw new CustomBusinessException(ErrorCode.RESERVATION_PERMISSION_ERROR);
        }

        return new ReservationDto(reservationEntity);
    }

    public Long selectWaitingOrder(MemberDto memberDto,
                                   Long reservationId
    ) {

        // 회원 조회
        MemberEntity memberEntity = memberRepository.findMemberByMemberId(memberDto.getId())
                .orElseThrow(() -> new CustomBusinessException(ErrorCode.MEMBER_NOT_FOUND));

        // 예약 조회
        ReservationEntity reservationEntity = reservationRepository.findReservation(reservationId)
                .orElseThrow(() -> new CustomBusinessException(ErrorCode.RESERVATION_NOT_FOUND));

        if (!memberEntity.getId().equals(reservationEntity.getMemberEntity().getId())) {
            throw new CustomBusinessException(ErrorCode.RESERVATION_PERMISSION_ERROR);
        }

        // 가게에 등록된 모든 예약 조회
        Long storeId = reservationEntity.getStoreId();
        List<ReservationEntity> reservationEntityList = reservationRepository.findReservationList(storeId);

        if (reservationEntityList.isEmpty()) {
            throw new CustomBusinessException(ErrorCode.RESERVATION_NOT_FOUND);
        }

        // 순번 조회(예약 생성 순으로 판단)
        List<Long> latestReservationId = reservationEntityList.stream()
                .sorted(Comparator.comparing(ReservationEntity::getCreatedAt))
                .map(ReservationEntity::getId)
                .toList();

        long index = 0;
        for (Long id : latestReservationId) {
            if (id.equals(reservationId)) {
                return index;
            }
            index++;
        }

        throw new CustomBusinessException(ErrorCode.RESERVATION_NOT_FOUND);
    }
}
