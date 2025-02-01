package com.cs2404.tablebuddy.reservation.dto;

import com.cs2404.tablebuddy.reservation.entity.ReservationStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class ReservationShowDto {

	@Getter
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class Response {

		private Long reservationId;
		private Long storeId;
		private ReservationStatus reservationStatus;
		private int peopleCount;
		private LocalDateTime createdAt;
		private LocalDateTime modifiedAt;

		public Response(ReservationDto reservationDto) {
			this.reservationId = reservationDto.getId();
			this.storeId = reservationDto.getStoreId();
			this.reservationStatus = reservationDto.getReservationStatus();
			this.peopleCount = reservationDto.getPeopleCount();
			this.createdAt = reservationDto.getCreatedAt();
			this.modifiedAt = reservationDto.getModifiedAt();
		}
	}
}
