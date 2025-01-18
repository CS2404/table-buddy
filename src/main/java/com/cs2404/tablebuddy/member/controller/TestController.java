package com.cs2404.tablebuddy.member.controller;

import com.cs2404.tablebuddy.common.config.security.LoginMember;
import com.cs2404.tablebuddy.member.dto.MemberDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api-root-path}/v1/test")
@RequiredArgsConstructor
@Slf4j
public class TestController {

    @PostMapping("/select")
    public ResponseEntity<String> signUp(
            @LoginMember MemberDto loginMember) {

        log.info(loginMember.toString());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(""+loginMember.getId());
    }



}
