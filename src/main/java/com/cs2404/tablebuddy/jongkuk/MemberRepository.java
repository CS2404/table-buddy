package com.cs2404.tablebuddy.jongkuk;

import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class MemberRepository {

    private final EntityManager entityManager;

    public MemberRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public Long saveMember(MemberEntity newMember){
        entityManager.persist(newMember);

        log.info("memberId: {}",newMember.getId());
        return newMember.getId();
    }



}
