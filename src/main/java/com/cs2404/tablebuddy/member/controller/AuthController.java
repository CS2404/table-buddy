package com.cs2404.tablebuddy.member.controller;

import com.cs2404.tablebuddy.member.dto.MemberSignUpDto;
import com.cs2404.tablebuddy.member.entity.MemberRole;
import com.cs2404.tablebuddy.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api-root-path}/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity<MemberSignUpDto.Response> signUp(
            @Valid @RequestBody MemberSignUpDto.Request memberSignUpRequest) {

        Long savedMemberId = memberService.signUp(
                memberSignUpRequest.getEmail(),
                memberSignUpRequest.getPassword(),
                memberSignUpRequest.getName(),
                memberSignUpRequest.getPhoneNumber(),
                MemberRole.from(memberSignUpRequest.getRole())
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new MemberSignUpDto.Response(savedMemberId));
    }



}
