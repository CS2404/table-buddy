package com.cs2404.tablebuddy.reservation.controller;

import com.cs2404.tablebuddy.TestSecurityConfig;
import com.cs2404.tablebuddy.common.config.security.UserDetailsDto;
import com.cs2404.tablebuddy.member.dto.MemberDto;
import com.cs2404.tablebuddy.member.entity.MemberRole;
import com.cs2404.tablebuddy.reservation.dto.ReservationAddDto;
import com.cs2404.tablebuddy.reservation.entity.ReservationStatus;
import com.cs2404.tablebuddy.reservation.service.ReservationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.*;

@Import(TestSecurityConfig.class)
@WebMvcTest(controllers = ReservationController.class)
class ReservationControllerTest {

    @MockBean
    private ReservationService reservationService;

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Value("${api-root-path}")
    private String apiRootPath;

    private static final MemberDto customerMemberDto = MemberDto.builder()
            .id(1L)
            .role(MemberRole.CUSTOMER)
            .build();

    private static final MemberDto ownerMemberDto = MemberDto.builder()
            .id(2L)
            .role(MemberRole.OWNER)
            .build();

    private static final UserDetailsDto customerUser = new UserDetailsDto(customerMemberDto, customerMemberDto.getAuthorities());
    private static final UserDetailsDto ownerUser = new UserDetailsDto(ownerMemberDto, ownerMemberDto.getAuthorities());

    @Test
    public void 줄서기_등록_테스트() throws Exception {

        // given

        // 로그인 사용자 시큐리티 세팅
        Authentication customerMemberAuthentication = new UsernamePasswordAuthenticationToken(
                customerUser,
                null,
                customerUser.getAuthorities()
        );
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(customerMemberAuthentication);
        SecurityContextHolder.setContext(context);

        ReservationAddDto.Request addRequest = ReservationAddDto.Request.builder()
                .storeId(1L)
                .peopleCount(2)
                .build();
        ReservationAddDto.Response response = new ReservationAddDto.Response(1L);


        given(
                reservationService.addReservation(
                        customerMemberDto,
                        addRequest.getStoreId(),
                        addRequest.getReservationStatus(),
                        addRequest.getPeopleCount()
                )
        ).willReturn(1L);


        // when
        ResultActions resultActions = mockMvc.perform(
                post(apiRootPath + "/v1/reservation/waiting")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addRequest))
        );

        // then
        verify(reservationService).addReservation(
                        any(MemberDto.class),
                        any(Long.class),
                        any(ReservationStatus.class),
                        anyInt()
                );

        resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.reservationId").value(1));

    }

    @Test
    public void 줄서기_삭제_테스트() throws Exception {

    }

    @Test
    public void 줄서기_수정_테스트() throws Exception {

    }

    @Test
    public void 줄서기_조회_테스트() throws Exception {

    }

    @Test
    public void 줄서기_대기순번_테스트() throws Exception {

    }

    @Test
    public void 사장이_줄서기_승인_테스트() throws Exception {

    }

}