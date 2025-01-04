package com.cs2404.tablebuddy.common.config.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.cs2404.tablebuddy.common.exception.CustomBusinessException;
import com.cs2404.tablebuddy.common.exception.ErrorCode;
import com.cs2404.tablebuddy.member.dto.MemberDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtils {
    public static final String AUTH_HEADER_TOKEN_DELIMITER = " ";
    public static final String TOKEN_AUTH_HEADER_NAME = "authorization";
    private final JwtProperty jwtProperty;

    public Long getMemberIdFromDecodedToken(String encodedAccessToken) {
        DecodedJWT decodedAccessToken = decodeAccessToken(encodedAccessToken);
        return Long.valueOf(decodedAccessToken.getSubject());
    }

    public String generateAccessToken(MemberDto memberDto) {
        return JWT.create()
                .withHeader(createJwtHeader())
                .withSubject(memberDto.getId().toString())
                .withExpiresAt(createExpiredDateBySecond(jwtProperty.getAccessTokenExpireSecond()))
                .sign(Algorithm.HMAC256(jwtProperty.getAccessTokenSecret()));
    }

    public String generateRefreshToken(MemberDto memberDto) {
        return JWT.create()
                .withHeader(createJwtHeader())
                .withSubject(memberDto.getId().toString())
                .withExpiresAt(createExpiredDateBySecond(jwtProperty.getRefreshTokenExpireSecond()))
                .sign(Algorithm.HMAC256(jwtProperty.getRefreshTokenSecret()));
    }

    public String generateRefreshToken(MemberDto memberDto, long remainingExpiredMinute) {
        return JWT.create()
                .withHeader(createJwtHeader())
                .withSubject(memberDto.getId().toString())
                .withExpiresAt(createExpiredDateBySecond(jwtProperty.getRefreshTokenExpireSecond()))
                .sign(Algorithm.HMAC256(jwtProperty.getRefreshTokenSecret()));
    }

    public DecodedJWT decodeAccessToken(String encodedAccessToken) {
        try {
            final JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(jwtProperty.getAccessTokenSecret())).build();
            return jwtVerifier.verify(encodedAccessToken);

        } catch (TokenExpiredException exception) {
            throw new CustomBusinessException(ErrorCode.TOKEN_EXPIRED); // 토큰 만료
        } catch (Exception e) {
            throw new CustomBusinessException(ErrorCode.TOKEN_IS_NOT_VALID);
        }
    }

    public DecodedJWT decodeRefreshToken(String encodedRefreshToken) {
        try {
            final JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(jwtProperty.getRefreshTokenSecret())).build();
            return jwtVerifier.verify(encodedRefreshToken);

        } catch (TokenExpiredException exception) {
            throw new CustomBusinessException(ErrorCode.TOKEN_EXPIRED); // 토큰 만료
        } catch (Exception e) {
            throw new CustomBusinessException(ErrorCode.TOKEN_IS_NOT_VALID);
        }
    }

    public LocalDateTime getExpireDateTimeFromDecodedToken(DecodedJWT decodedToken) {
        return LocalDateTime.ofInstant(decodedToken.getExpiresAtAsInstant(), ZoneId.systemDefault());
    }



    private Instant createExpiredDateBySecond(long second) {
        Instant now = Instant.now();
        return now.plus(second, ChronoUnit.SECONDS);
    }

    private static Map<String, Object> createJwtHeader() {
        Map<String, Object> header = new HashMap<>();
        header.put("typ", "JWT");
        header.put("alg", "HS256");
        return header;
    }



}
