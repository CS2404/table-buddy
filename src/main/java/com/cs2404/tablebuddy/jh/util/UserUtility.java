package com.cs2404.tablebuddy.jh.util;

import com.cs2404.tablebuddy.jh.dto.SignUpRequest;
import com.cs2404.tablebuddy.jh.dto.SignUpResponse;
import com.cs2404.tablebuddy.jh.entity.User;

public class UserUtility {
    public static User convertToUser(SignUpRequest signUpRequest) {
        return User.builder()
                .name(signUpRequest.getName())
                .email(signUpRequest.getEmail())
                .phoneNumber(signUpRequest.getPhoneNumber())
                .password(signUpRequest.getPassword())
                .role(signUpRequest.getRole())
                .build();
    }

    public static SignUpResponse converToSignUpResponse(User createdUser) {
        return new SignUpResponse(createdUser.getId());
    }
}
