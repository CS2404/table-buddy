package com.cs2404.tablebuddy.reservation.repository;

import com.cs2404.tablebuddy.reservation.entity.ReservationEntity;
import com.cs2404.tablebuddy.reservation.entity.ReservationStatus;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.cs2404.tablebuddy.reservation.entity.QReservationEntity.reservationEntity;


@Slf4j
@Repository
public class ReservationRepository {

    private final EntityManager entityManager;
    private final JPAQueryFactory queryFactory;

    public ReservationRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    public Long saveReservation(ReservationEntity reservationEntity) {
        entityManager.persist(reservationEntity);
        log.debug("reservationId: {}", reservationEntity.getId());
        return reservationEntity.getId();
    }

    public Optional<ReservationEntity> findReservation(Long reservationId) {
        return Optional.ofNullable(
            queryFactory
                    .select(reservationEntity)
                    .from(reservationEntity)
                    .where(
                            reservationEntity.id.eq(reservationId)
                    )
                    .fetchFirst()
        );
    }

    public List<ReservationEntity> findReservationList(Long storeId) {
        return queryFactory
                .select(reservationEntity)
                .from(reservationEntity)
                .where(
                        reservationEntity.storeId.eq(storeId)
                )
                .fetch();
    }

    public Integer countWaitingCustomer(Long storeId) {
        return queryFactory
                .select(reservationEntity.peopleCount.sum().coalesce(0))
                .from(reservationEntity)
                .where(
                        reservationEntity.storeId.eq(storeId),
                        reservationEntity.reservationStatus.eq(ReservationStatus.CONFIRMED)
                )
                .fetchOne();
    }
}
