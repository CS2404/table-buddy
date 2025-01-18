package com.cs2404.tablebuddy.member.entity;

import com.cs2404.tablebuddy.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Entity
@Table(name = "member_access_token")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MemberAccessTokenEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_access_token_id")
    private Long id;

    @Column(name = "access_token", updatable = false, nullable = false)
    private String accessToken;

    @Column(name = "access_token_expired_at", updatable = false, nullable = false)
    private LocalDateTime accessTokenExpiredAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id",
            updatable = false,
            nullable = false,
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private MemberEntity memberEntity;


    @Builder
    public MemberAccessTokenEntity(Long id, String accessToken, LocalDateTime accessTokenExpiredAt, MemberEntity memberEntity) {
        this.id = id;
        this.accessToken = accessToken;
        this.accessTokenExpiredAt = accessTokenExpiredAt;
        this.memberEntity = memberEntity;
    }

}
