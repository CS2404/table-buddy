package com.cs2404.tablebuddy.reservation.entity;

import com.cs2404.tablebuddy.common.entity.BaseTimeEntity;
import com.cs2404.tablebuddy.member.entity.MemberEntity;
import com.cs2404.tablebuddy.store.entity.StoreEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "reservation")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ReservationEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id")
    private Long id;

    // 예약 상태
    @Enumerated(EnumType.STRING)
    @Column(name = "reservation_status", length = 20, nullable = false)
    private ReservationStatus reservationStatus;

    // 총 예약 인원
    @Column(name = "people_count", nullable = false)
    private int peopleCount;

    @Enumerated(EnumType.STRING)
    @Column(name = "is_deleted", length = 1, nullable = false)
    private DeleteStatus isDeleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id",
            updatable = false,
            nullable = false,
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private MemberEntity memberEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id",
            updatable = false,
            nullable = false,
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private StoreEntity storeEntity;

    @Builder
    public ReservationEntity(Long id, ReservationStatus reservationStatus, int peopleCount, DeleteStatus isDeleted, MemberEntity memberEntity, StoreEntity storeEntity) {
        this.id = id;
        this.reservationStatus = reservationStatus;
        this.peopleCount = peopleCount;
        this.isDeleted = isDeleted;
        this.memberEntity = memberEntity;
        this.storeEntity = storeEntity;
    }

}
