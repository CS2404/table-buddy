package com.cs2404.tablebuddy.reservation.controller;

import com.cs2404.tablebuddy.TestSecurityConfig;
import com.cs2404.tablebuddy.common.config.security.UserDetailsDto;
import com.cs2404.tablebuddy.member.dto.MemberDto;
import com.cs2404.tablebuddy.member.entity.MemberRole;
import com.cs2404.tablebuddy.reservation.dto.*;
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

import java.time.LocalDateTime;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

        Long reservationId = 1L;

        given(
                reservationService.deleteReservation(
                        customerMemberDto,
                        reservationId
                )
        ).willReturn(1L);


        // when
        ResultActions resultActions = mockMvc.perform(
                delete(apiRootPath + "/v1/reservation/waiting/" + reservationId)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        verify(reservationService).deleteReservation(
                any(MemberDto.class),
                any(Long.class)
        );

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reservationId").value(1));

    }

    @Test
    public void 줄서기_수정_테스트() throws Exception {
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

        Long reservationId = 1L;
        ReservationEditDto.Request request = ReservationEditDto.Request.builder()
                .peopleCount(2)
                .build();
        ReservationEditDto.Response response = new ReservationEditDto.Response(1L);


        given(
                reservationService.editReservation(
                        customerMemberDto,
                        reservationId,
                        request.getPeopleCount()
                )
        ).willReturn(1L);


        // when
        ResultActions resultActions = mockMvc.perform(
                put(apiRootPath + "/v1/reservation/waiting/" + reservationId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        );

        // then
        verify(reservationService).editReservation(
                any(MemberDto.class),
                any(Long.class),
                anyInt()
        );

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reservationId").value(1));

    }

    @Test
    public void 줄서기_조회_테스트() throws Exception {

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

        Long reservationId = 1L;
        ReservationShowDto.Response response = ReservationShowDto.Response.builder()
                .reservationId(reservationId)
                .storeId(1L)
                .reservationStatus(ReservationStatus.PENDING)
                .peopleCount(2)
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .build();


        given(
                reservationService.findReservation(
                        customerMemberDto,
                        reservationId
                )
        ).willReturn(
                ReservationDto.builder()
                        .id(response.getReservationId())
                        .storeId(response.getStoreId())
                        .reservationStatus(response.getReservationStatus())
                        .peopleCount(response.getPeopleCount())
                        .createdAt(response.getCreatedAt())
                        .modifiedAt(response.getModifiedAt())
                        .build()
        );


        // when
        ResultActions resultActions = mockMvc.perform(
                get(apiRootPath + "/v1/reservation/waiting/" + reservationId)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        verify(reservationService).findReservation(
                any(MemberDto.class),
                any(Long.class)
        );

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reservationId").value(response.getReservationId()))
                .andExpect(jsonPath("$.storeId").value(response.getStoreId()))
                .andExpect(jsonPath("$.reservationStatus").value(response.getReservationStatus().toString()))
                .andExpect(jsonPath("$.peopleCount").value(response.getPeopleCount()))
        ;

    }

    @Test
    public void 줄서기_대기순번_테스트() throws Exception {
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

        Long reservationId = 1L;
        Long waitingOrder = 1L;
        ReservationWaitingOrderDto.Response response = new ReservationWaitingOrderDto.Response(waitingOrder);


        given(
                reservationService.selectWaitingOrder(
                        customerMemberDto,
                        reservationId
                )
        ).willReturn(waitingOrder);


        // when
        ResultActions resultActions = mockMvc.perform(
                get(apiRootPath + "/v1/reservation/waiting/" + reservationId + "/order")
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        verify(reservationService).selectWaitingOrder(
                any(MemberDto.class),
                any(Long.class)
        );

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.waitingOrder").value(waitingOrder));

    }

    @Test
    public void 사장이_줄서기_승인_테스트() throws Exception {
        // given

        // 로그인 사용자 시큐리티 세팅
        Authentication customerMemberAuthentication = new UsernamePasswordAuthenticationToken(
                ownerUser,
                null,
                ownerUser.getAuthorities()
        );
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(customerMemberAuthentication);
        SecurityContextHolder.setContext(context);

        Long reservationId = 1L;
        ReservationApproveDto.Response response = new ReservationApproveDto.Response(reservationId);


        given(
                reservationService.approveReservation(
                        ownerMemberDto,
                        reservationId
                )
        ).willReturn(reservationId);


        // when
        ResultActions resultActions = mockMvc.perform(
                put(apiRootPath + "/v1/reservation/waiting/" + reservationId + "/approval")
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        verify(reservationService).approveReservation(
                any(MemberDto.class),
                any(Long.class)
        );

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reservationId").value(response.getReservationId()));

    }

}