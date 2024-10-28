package com.cs2404.tablebuddy.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class MemberLoginDto {

	@Getter
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class Request {
		@NotEmpty
		@Email
		private String email;

		@NotEmpty
		private String password;

		@Builder
		public Request(String email, String password) {
			this.email = email;
			this.password = password;
		}
	}

	@Getter
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class Response {
		@NotNull
		private String accessToken;

		@NotNull
		private String refreshToken;

		@Builder
		public Response(String accessToken, String refreshToken) {
			this.accessToken = accessToken;
			this.refreshToken = refreshToken;
		}
	}
}
