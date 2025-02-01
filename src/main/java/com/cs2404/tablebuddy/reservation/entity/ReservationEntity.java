package com.cs2404.tablebuddy.reservation.entity;

import com.cs2404.tablebuddy.common.entity.BaseTimeEntity;
import com.cs2404.tablebuddy.member.entity.MemberEntity;
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

    // 가게 엔티티의 외래키는 수동으로 관리
    @Column(name = "store_id", nullable = false)
    private Long storeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id",
            updatable = false,
            nullable = false,
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private MemberEntity memberEntity;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "store_id",
//            updatable = false,
//            nullable = false,
//            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
//    private StoreEntity storeEntity;

    @Builder
    public ReservationEntity(Long id, ReservationStatus reservationStatus, int peopleCount, DeleteStatus isDeleted, Long storeId, MemberEntity memberEntity) {
        this.id = id;
        this.reservationStatus = reservationStatus;
        this.peopleCount = peopleCount;
        this.isDeleted = isDeleted;
        this.storeId = storeId;
        this.memberEntity = memberEntity;
    }

    public void cancelReservation() {
        this.reservationStatus = ReservationStatus.REJECTED;
        this.isDeleted = DeleteStatus.Y;
    }

    public void changePeopleCount(int peopleCount) {
        this.peopleCount = peopleCount;
    }

    public void changeReservationStatus(ReservationStatus reservationStatus) {
        this.reservationStatus = reservationStatus;
    }
}
