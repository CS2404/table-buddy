package com.cs2404.tablebuddy.reservation.entity;

public enum ReservationStatus {
    PENDING, // 예약 요청이 생성되었지만 아직 승인되지 않음
    REJECTED, // 예약 취소됨  // TODO: 사용자가 스스로 취소하는 경우도 구분해야되는지 검토 필요(CANCELLED)
    CONFIRMED, // 예약 확인됨, 승인됨
    COMPLETED, // 완료됨
}
