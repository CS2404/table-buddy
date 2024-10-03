package com.cs2404.tablebuddy.jh.controller;

import com.cs2404.tablebuddy.jh.dto.SignUpRequest;
import com.cs2404.tablebuddy.jh.dto.SignUpResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/v1/users")
public interface UserController {
    @PostMapping
    ResponseEntity<SignUpResponse> signup(@RequestBody @Valid SignUpRequest signUpRequest);
}
