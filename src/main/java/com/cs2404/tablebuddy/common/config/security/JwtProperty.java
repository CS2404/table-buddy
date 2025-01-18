package com.cs2404.tablebuddy.common.config.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "jwt")
@Getter
@Setter
public class JwtProperty {
    private long accessTokenExpireSecond;
    private String accessTokenSecret;
    private long refreshTokenExpireSecond;
    private String refreshTokenSecret;
}
