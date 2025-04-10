package com.cs2404.tablebuddy.store.entity;

import com.cs2404.tablebuddy.common.entity.BaseTimeEntity;
import com.cs2404.tablebuddy.member.entity.MemberEntity;
import com.cs2404.tablebuddy.store.dto.UpdateStoreDto;
import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.FeatureDescriptor;
import java.util.Arrays;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@Table(name = "store")
public class StoreEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "member_id",
            nullable = false,
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private MemberEntity member;

    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "category", length = 20, nullable = false)
    private Category category;

    @Column(name = "max_waiting_capacity", nullable = false)
    private Long maxWaitingCapacity;         // 최대 대기인원

    @Enumerated(EnumType.STRING)
    @Column(name = "is_deleted", length = 1, nullable = false)
    private DeleteStatus isDeleted;

    @Builder
    public StoreEntity(Long id,
                       MemberEntity member,
                       String name,
                       Category category,
                       Long maxWaitingCapacity,
                       DeleteStatus isDeleted
    ) {
        this.id = id;
        this.member = member;
        this.name = name;
        this.category = category;
        this.maxWaitingCapacity = maxWaitingCapacity;
        this.isDeleted = isDeleted;
    }

    public void updateStore(UpdateStoreDto.Request updateStoreRequest) {
        BeanUtils.copyProperties(updateStoreRequest, this, getNullPropertyNames(updateStoreRequest));
    }

    private String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        return Arrays.stream(src.getPropertyDescriptors())
                .map(FeatureDescriptor::getName)
                .filter(value -> src.getPropertyValue(value) == null)
                .toArray(String[]::new);
    }

    // 예약 가능 인원 조회
    public long getAvailableReservationCount(int waitingCustomerCount) {
        return maxWaitingCapacity - waitingCustomerCount;
    }
}
