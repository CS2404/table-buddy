package com.cs2404.tablebuddy.reservation.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ReservationWaitingOrderDto {

	@Getter
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class Response {
		private Long waitingOrder; // 0부터 시작

		public Response(Long waitingOrder) {
			this.waitingOrder = waitingOrder;
		}
	}
}
