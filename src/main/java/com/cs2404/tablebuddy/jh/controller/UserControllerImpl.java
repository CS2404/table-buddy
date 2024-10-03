package com.cs2404.tablebuddy.jh.controller;

import com.cs2404.tablebuddy.jh.dto.SignUpRequest;
import com.cs2404.tablebuddy.jh.dto.SignUpResponse;
import com.cs2404.tablebuddy.jh.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserControllerImpl implements UserController {
    private final UserService userService;

    @Override
    public ResponseEntity<SignUpResponse> signup(SignUpRequest request) {
        SignUpResponse response = userService.signup(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
