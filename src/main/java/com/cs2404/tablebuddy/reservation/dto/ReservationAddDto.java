package com.cs2404.tablebuddy.reservation.dto;

import com.cs2404.tablebuddy.reservation.entity.ReservationStatus;
import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ReservationAddDto {
	@Getter
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class Request {

		private final ReservationStatus reservationStatus = ReservationStatus.PENDING;

		@NotNull(message = "storeId는 필수 값입니다.")
		private Long storeId;

		@Min(value = 1, message = "peopleCount는 1 이상이어야 합니다.")
		@Max(value = 1000, message = "peopleCount는 1000 이하여야 합니다.")
		private int peopleCount;

		@Builder
		public Request(Long storeId, int peopleCount) {
			this.storeId = storeId;
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
