package com.cs2404.tablebuddy.reservation.controller;

import com.cs2404.tablebuddy.common.config.security.LoginMember;
import com.cs2404.tablebuddy.common.exception.CustomBusinessException;
import com.cs2404.tablebuddy.common.exception.ErrorCode;
import com.cs2404.tablebuddy.member.dto.MemberDto;
import com.cs2404.tablebuddy.reservation.dto.*;
import com.cs2404.tablebuddy.reservation.service.ReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api-root-path}/v1/reservation")
@RequiredArgsConstructor
@Slf4j
public class ReservationController {

    private final ReservationService reservationService;

    // 줄서기 등록
    @PostMapping("/waiting")
    public ResponseEntity<ReservationAddDto.Response> addWaiting(
            @LoginMember MemberDto loginMember,
            @Valid @RequestBody ReservationAddDto.Request reservationAddRequest
    ) {
        Long reservationId = reservationService.addReservation(
                loginMember,
                reservationAddRequest.getStoreId(),
                reservationAddRequest.getReservationStatus(),
                reservationAddRequest.getPeopleCount()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ReservationAddDto.Response(reservationId));
    }

    // 줄서기 등록 취소
    @DeleteMapping("/waiting/{reservationId}")
    public ResponseEntity<ReservationDeleteDto.Response> cancelWaiting(
            @LoginMember MemberDto loginMember,
            @PathVariable Long reservationId
    ) {
        // TODO: 고객만 사용가능한 API로 할지 검토
        // TODO: 사장이 취소하는 예약과 구분할지 검토

        Long id = reservationService.deleteReservation(
                loginMember,
                reservationId
        );

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ReservationDeleteDto.Response(id));
    }

    // 줄서기 수정
    // - count 수정
    @PutMapping("/waiting/{reservationId}")
    public ResponseEntity<ReservationEditDto.Response> editWaiting(
            @LoginMember MemberDto loginMember,
            @PathVariable Long reservationId,
            @Valid @RequestBody ReservationEditDto.Request reservationEditRequest
    ) {
        Long id = reservationService.editReservation(
                loginMember,
                reservationId,
                reservationEditRequest.getPeopleCount()
        );

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ReservationEditDto.Response(id));
    }

    // 줄서기 조회
    @GetMapping("/waiting/{reservationId}")
    public ResponseEntity<ReservationShowDto.Response> findWaiting(
            @LoginMember MemberDto loginMember,
            @PathVariable Long reservationId
    ) {
        ReservationDto reservationDto = reservationService.findReservation(
                loginMember,
                reservationId
        );

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ReservationShowDto.Response(reservationDto));
    }

    // 대기 순번 조회
    @GetMapping("/waiting/order/{reservationId}")
    public ResponseEntity<ReservationWaitingOrderDto.Response> selectWaitingOrder(
            @LoginMember MemberDto loginMember,
            @PathVariable Long reservationId
    ) {
        Long waitingOrder = reservationService.selectWaitingOrder(
                loginMember,
                reservationId
        );

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ReservationWaitingOrderDto.Response(waitingOrder));
    }

    // TODO: 가게가 줄서기 신청 승인
//    @PutMapping("/waiting/{reservationId}")
//    public ResponseEntity<ReservationDeleteDto.Response> approveWaiting(
//            @LoginMember MemberDto loginMember,
//            @PathVariable Long reservationId
//    ) {
//        // 사장만 사용 가능한 API
//        if (!loginMember.isOwner()) {
//            throw new CustomBusinessException(ErrorCode.RESERVATION_PERMISSION_ERROR);
//        }
//
//        Long id = reservationService.deleteReservation(
//                loginMember,
//                reservationId
//        );
//
//        return ResponseEntity
//                .status(HttpStatus.OK)
//                .body(new ReservationDeleteDto.Response(id));
//    }

}
