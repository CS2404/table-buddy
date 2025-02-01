package com.cs2404.tablebuddy.reservation.dto;

import com.cs2404.tablebuddy.member.dto.MemberDto;
import com.cs2404.tablebuddy.reservation.entity.DeleteStatus;
import com.cs2404.tablebuddy.reservation.entity.ReservationEntity;
import com.cs2404.tablebuddy.reservation.entity.ReservationStatus;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReservationDto {

    private Long id;
    private ReservationStatus reservationStatus;
    private int peopleCount;
    private DeleteStatus isDeleted;
    private MemberDto memberDto;
    private Long storeId;

    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    @Builder
    public ReservationDto(Long id, ReservationStatus reservationStatus, int peopleCount, DeleteStatus isDeleted, MemberDto memberDto, Long storeId, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.id = id;
        this.reservationStatus = reservationStatus;
        this.peopleCount = peopleCount;
        this.isDeleted = isDeleted;
        this.memberDto = memberDto;
        this.storeId = storeId;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }

    public ReservationDto(ReservationEntity reservationEntity) {
        this.id = reservationEntity.getId();
        this.reservationStatus = reservationEntity.getReservationStatus();
        this.peopleCount = reservationEntity.getPeopleCount();
        this.isDeleted = reservationEntity.getIsDeleted();
        if (reservationEntity.getMemberEntity() != null) {
            this.memberDto = new MemberDto(reservationEntity.getMemberEntity());
        }
        this.storeId = reservationEntity.getStoreId();
        this.createdAt = reservationEntity.getCreatedAt();
        this.modifiedAt = reservationEntity.getModifiedAt();
    }

}
