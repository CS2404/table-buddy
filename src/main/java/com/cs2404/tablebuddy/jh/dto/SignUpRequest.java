package com.cs2404.tablebuddy.jh.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class SignUpRequest {
    @NotEmpty
    private String name;
    @NotEmpty
    @Email
    private String email;
    @NotEmpty
    private String password;
    @NotEmpty
    private String phoneNumber;
    @NotEmpty
    private String role;
}
