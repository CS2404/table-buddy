package com.cs2404.tablebuddy.store.repository;


import com.cs2404.tablebuddy.member.entity.MemberEntity;
import com.cs2404.tablebuddy.store.entity.BusinessHourEntity;
import com.cs2404.tablebuddy.store.entity.StoreEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Member;
import java.util.Optional;

import static com.cs2404.tablebuddy.store.entity.QStoreEntity.storeEntity;

@Slf4j
@Repository
public class StoreRepository {

    private final EntityManager entityManager;
    private final JPAQueryFactory queryFactory;

    public StoreRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    public Long saveStore(StoreEntity newStore) {
        entityManager.persist(newStore);
        log.debug("storeId: {}", newStore.getId());
        return newStore.getId();
    }

    public Optional<StoreEntity> findById(Long storeId) {
        return Optional.ofNullable(
                queryFactory
                        .select(storeEntity)
                        .from(storeEntity)
                        .where(storeEntity.id.eq(storeId))
                        .fetchFirst()
        );
    }

    public Optional<StoreEntity> findByMember(MemberEntity member) {
        return Optional.ofNullable(
                queryFactory
                        .select(storeEntity)
                        .from(storeEntity)
                        .where(storeEntity.member.eq(member)) // 파라미터로 전달된 member 사용
                        .fetchFirst() // 첫 번째 결과만 반환
        );
    }

    public void saveBusinessHour(BusinessHourEntity newBusinessHourEntity) {
        entityManager.persist(newBusinessHourEntity);
        log.debug("businessHourId: {}", newBusinessHourEntity.getId());
    }
}
