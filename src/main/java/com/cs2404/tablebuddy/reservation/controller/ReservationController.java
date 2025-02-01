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
            @LoginMember MemberDto loginCustomer,
            @Valid @RequestBody ReservationAddDto.Request reservationAddRequest
    ) {
        // 고객만 사용 가능한 API
        if (!loginCustomer.isCustomer()) {
            throw new CustomBusinessException(ErrorCode.RESERVATION_PERMISSION_ERROR);
        }

        Long reservationId = reservationService.addReservation(
                loginCustomer,
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
            @LoginMember MemberDto loginCustomer,
            @PathVariable("reservationId") Long reservationId
    ) {
        // 고객만 사용 가능한 API
        if (!loginCustomer.isCustomer()) {
            throw new CustomBusinessException(ErrorCode.RESERVATION_PERMISSION_ERROR);
        }

        Long id = reservationService.deleteReservation(
                loginCustomer,
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
            @LoginMember MemberDto loginCustomer,
            @PathVariable("reservationId") Long reservationId,
            @Valid @RequestBody ReservationEditDto.Request reservationEditRequest
    ) {
        // 고객만 사용 가능한 API
        if (!loginCustomer.isCustomer()) {
            throw new CustomBusinessException(ErrorCode.RESERVATION_PERMISSION_ERROR);
        }

        Long id = reservationService.editReservation(
                loginCustomer,
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
            @LoginMember MemberDto loginCustomer,
            @PathVariable("reservationId") Long reservationId
    ) {
        // 고객만 사용 가능한 API
        if (!loginCustomer.isCustomer()) {
            throw new CustomBusinessException(ErrorCode.RESERVATION_PERMISSION_ERROR);
        }

        ReservationDto reservationDto = reservationService.findReservation(
                loginCustomer,
                reservationId
        );

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ReservationShowDto.Response(reservationDto));
    }

    // 대기 순번 조회
    @GetMapping("/waiting/{reservationId}/order")
    public ResponseEntity<ReservationWaitingOrderDto.Response> selectWaitingOrder(
            @LoginMember MemberDto loginCustomer,
            @PathVariable("reservationId") Long reservationId
    ) {
        Long waitingOrder = reservationService.selectWaitingOrder(
                loginCustomer,
                reservationId
        );

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ReservationWaitingOrderDto.Response(waitingOrder));
    }

    // 사장(가게)이 줄서기 신청을 승인하는 용도
    @PutMapping("/waiting/{reservationId}/approval")
    public ResponseEntity<ReservationApproveDto.Response> approveWaiting(
            @LoginMember MemberDto loginOwner,
            @PathVariable("reservationId") Long reservationId
    ) {
        // 사장만 사용 가능한 API
        if (!loginOwner.isOwner()) {
            throw new CustomBusinessException(ErrorCode.RESERVATION_PERMISSION_ERROR);
        }

        Long id = reservationService.approveReservation(
                loginOwner,
                reservationId
        );

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ReservationApproveDto.Response(id));
    }

}
