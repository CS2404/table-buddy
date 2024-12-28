package com.cs2404.tablebuddy.member.controller;

import com.cs2404.tablebuddy.TestSecurityConfig;
import com.cs2404.tablebuddy.member.dto.MemberSignUpDto;
import com.cs2404.tablebuddy.member.entity.MemberRole;
import com.cs2404.tablebuddy.member.service.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * @WebMvcTest : 웹 계층과 관련된 Bean 등록
 * <p>
 * 컨트롤러 에서는 WebMvcTest 방식
 * 그외 유닛테스트
 */
@Import(TestSecurityConfig.class)
@WebMvcTest(controllers = AuthController.class)
class AuthControllerTest {

    @MockBean
    private MemberService memberService;

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Value("${api-root-path}")
    private String apiRootPath;

    @Test
    public void singup_테스트() throws Exception {
        // given
        MemberSignUpDto.Request memberSignupRequest = MemberSignUpDto.Request.builder()
                .name("userName")
                .phoneNumber("010-1234-1234")
                .password("testPassword")
                .email("abc@abc.com")
                .role(MemberRole.CUSTOMER.toString())
                .build();
        MemberSignUpDto.Response memberResponse = new MemberSignUpDto.Response(1L);

        given(
                memberService.signUp(
                        memberSignupRequest.getEmail(),
                        memberSignupRequest.getPassword(),
                        memberSignupRequest.getName(),
                        memberSignupRequest.getPhoneNumber(),
                        MemberRole.from(memberSignupRequest.getRole())
                )
        ).willReturn(1L);

        // when
        ResultActions resultActions = mockMvc.perform(post(apiRootPath + "/v1/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(memberSignupRequest))
        );

        resultActions.andDo(print());

        // then
        resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));
    }
}

