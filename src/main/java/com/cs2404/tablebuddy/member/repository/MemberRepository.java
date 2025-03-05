package com.cs2404.tablebuddy.member.repository;

import com.cs2404.tablebuddy.common.entity.DeleteStatus;
import com.cs2404.tablebuddy.member.entity.MemberAccessTokenEntity;
import com.cs2404.tablebuddy.member.entity.MemberEntity;
import com.cs2404.tablebuddy.member.entity.MemberRefreshTokenEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.cs2404.tablebuddy.member.entity.QMemberEntity.memberEntity;


@Slf4j
@Repository
public class MemberRepository {

    private final EntityManager entityManager;
    private final JPAQueryFactory queryFactory;

    public MemberRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    public Long saveMember(MemberEntity newMember) {
        entityManager.persist(newMember);
        log.debug("memberId: {}", newMember.getId());
        return newMember.getId();
    }


    public Optional<MemberEntity> findMemberByEmail(String email) {
        return Optional.ofNullable(
                queryFactory
                        .select(memberEntity)
                        .from(memberEntity)
                        .where(
                                memberEntity.email.eq(email),
                                memberEntity.isDeleted.eq(DeleteStatus.N)
                        )
                        .fetchFirst()
        );
    }

    public Optional<MemberEntity> findMemberByMemberId(Long memberId) {
        return Optional.ofNullable(
                queryFactory.select(memberEntity)
                        .from(memberEntity)
                        .where(
                                memberEntity.id.eq(memberId)
                        )
                        .fetchOne()
        );
    }

    public void saveMemberAccessTokenEntity(MemberAccessTokenEntity memberAccessTokenEntity) {
        entityManager.persist(memberAccessTokenEntity);
    }

    public void saveMemberRefreshTokenEntity(MemberRefreshTokenEntity memberRefreshTokenEntity) {
        entityManager.persist(memberRefreshTokenEntity);
    }
}
