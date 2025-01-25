package com.cs2404.tablebuddy.reservation.service;

import com.cs2404.tablebuddy.common.exception.CustomBusinessException;
import com.cs2404.tablebuddy.common.exception.ErrorCode;
import com.cs2404.tablebuddy.member.dto.MemberDto;
import com.cs2404.tablebuddy.member.entity.MemberEntity;
import com.cs2404.tablebuddy.member.repository.MemberRepository;
import com.cs2404.tablebuddy.reservation.dto.ReservationDto;
import com.cs2404.tablebuddy.reservation.entity.DeleteStatus;
import com.cs2404.tablebuddy.reservation.entity.ReservationEntity;
import com.cs2404.tablebuddy.reservation.entity.ReservationStatus;
import com.cs2404.tablebuddy.reservation.repository.ReservationRepository;
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
    public Long deleteReservation(MemberDto memberDto, Long reservationId) {

        // 회원 조회
        MemberEntity memberEntity = memberRepository.findMemberByMemberId(memberDto.getId())
                .orElseThrow(() -> new CustomBusinessException(ErrorCode.MEMBER_NOT_FOUND));

        // 예약 조회
        ReservationEntity reservationEntity = reservationRepository.findReservation(reservationId)
                .orElseThrow(() -> new CustomBusinessException(ErrorCode.RESERVATION_NOT_FOUND));

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
                                int peopleCount) {

        // 회원 조회
        MemberEntity memberEntity = memberRepository.findMemberByMemberId(memberDto.getId())
                .orElseThrow(() -> new CustomBusinessException(ErrorCode.MEMBER_NOT_FOUND));

        // 예약 조회
        ReservationEntity reservationEntity = reservationRepository.findReservation(reservationId)
                .orElseThrow(() -> new CustomBusinessException(ErrorCode.RESERVATION_NOT_FOUND));

        if (!memberEntity.getId().equals(reservationEntity.getMemberEntity().getId())) {
            throw new CustomBusinessException(ErrorCode.RESERVATION_PERMISSION_ERROR);
        }

        if (reservationEntity.getPeopleCount() != peopleCount) {
            reservationEntity.changePeopleCount(peopleCount);
        }

        return reservationId;
    }

    public ReservationDto findReservation(MemberDto memberDto,
                                          Long reservationId) {

        // 회원 조회
        MemberEntity memberEntity = memberRepository.findMemberByMemberId(memberDto.getId())
                .orElseThrow(() -> new CustomBusinessException(ErrorCode.MEMBER_NOT_FOUND));

        // 예약 조회
        ReservationEntity reservationEntity = reservationRepository.findReservation(reservationId)
                .orElseThrow(() -> new CustomBusinessException(ErrorCode.RESERVATION_NOT_FOUND));

        if (!memberEntity.getId().equals(reservationEntity.getMemberEntity().getId())) {
            throw new CustomBusinessException(ErrorCode.RESERVATION_PERMISSION_ERROR);
        }

        return new ReservationDto(reservationEntity);
    }

    public Long selectWaitingOrder(MemberDto memberDto,
                                   Long reservationId) {

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
