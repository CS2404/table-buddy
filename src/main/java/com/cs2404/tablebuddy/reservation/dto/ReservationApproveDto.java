package com.cs2404.tablebuddy.reservation.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ReservationApproveDto {

	@Getter
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class Response {
		private Long reservationId;

		public Response(Long reservationId) {
			this.reservationId = reservationId;
		}
	}
}
