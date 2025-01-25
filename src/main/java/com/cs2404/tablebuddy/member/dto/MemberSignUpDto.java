package com.cs2404.tablebuddy.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class MemberSignUpDto {
	@Getter
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class Request {
		@NotEmpty
		private String name;
		@NotEmpty
		@Email
		private String email;
		@NotEmpty
		private String password;
		@NotEmpty
		@Pattern(regexp = "^\\d{3}-\\d{4}-\\d{4}$", message = "Phone number must be in the format 010-1234-5678")
		private String phoneNumber;
		@NotEmpty
		private String role;

		@Builder
		public Request(String name, String email, String password, String phoneNumber, String role) {
			this.name = name;
			this.email = email;
			this.password = password;
			this.phoneNumber = phoneNumber;
			this.role = role;
		}
	}

	@Getter
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class Response {
		private Long id;

		public Response(Long id) {
			this.id = id;
		}
	}
}
