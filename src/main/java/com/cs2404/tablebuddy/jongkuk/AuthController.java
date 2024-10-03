package com.cs2404.tablebuddy.jongkuk;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api-root-path}/v1/auth") // => /api/v1/auth
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public SignUpResponseDto signUp(@RequestBody SignUpRequestDto signUpRequestDto) {

        // 검증 : signUpRequestDto
        signUpRequestDto.isValid();

        // 회원 가입
        Long savedMemberId = authService.signUp(signUpRequestDto);

        return new SignUpResponseDto(savedMemberId);
    }






}
