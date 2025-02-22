package com.cs2404.tablebuddy.reservation.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ReservationEditDto {
	@Getter
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class Request {

		@Min(value = 1, message = "peopleCount는 1 이상이어야 합니다.")
		@Max(value = 1000, message = "peopleCount는 1000 이하여야 합니다.")
		private int peopleCount;

		@Builder
		public Request(int peopleCount) {
			this.peopleCount = peopleCount;
		}

	}

	@Getter
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class Response {
		private Long reservationId;

		public Response(Long reservationId) {
			this.reservationId = reservationId;
		}
	}
}
