package com.cs2404.tablebuddy.store.repository;


import com.cs2404.tablebuddy.member.entity.MemberEntity;
import com.cs2404.tablebuddy.store.entity.BusinessHourEntity;
import com.cs2404.tablebuddy.store.entity.StoreEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
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
    }

    public Page<StoreEntity> findAll(Pageable pageable) {
        List<StoreEntity> stores = queryFactory
                .selectFrom(storeEntity)
                .offset(pageable.getOffset())   // 시작 위치
                .limit(pageable.getPageSize())  // 한 페이지 크기
                .fetch();                       // 데이터 조회

        // 전체 데이터 개수 조회
        long total = queryFactory
                .select(storeEntity.count())    // count 쿼리
                .from(storeEntity)
                .fetchOne();

        return new PageImpl<>(stores, pageable, total);
    }

}
