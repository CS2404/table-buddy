package com.cs2404.tablebuddy.member.entity;

import com.cs2404.tablebuddy.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Entity
@Table(name = "member_refresh_token")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MemberRefreshTokenEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_refresh_token_id")
    private Long id;

    @Column(name = "refresh_token", updatable = false, nullable = false)
    private String refreshToken;

    @Column(name = "refresh_token_expired_at", updatable = false, nullable = false)
    private LocalDateTime refreshTokenExpiredAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id",
            updatable = false,
            nullable = false,
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private MemberEntity memberEntity;


    @Builder
    public MemberRefreshTokenEntity(Long id, String refreshToken, LocalDateTime refreshTokenExpiredAt, MemberEntity memberEntity) {
        this.id = id;
        this.refreshToken = refreshToken;
        this.refreshTokenExpiredAt = refreshTokenExpiredAt;
        this.memberEntity = memberEntity;
    }
}
