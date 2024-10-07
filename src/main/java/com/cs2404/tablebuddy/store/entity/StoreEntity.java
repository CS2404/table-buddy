package com.cs2404.tablebuddy.store.entity;

import com.cs2404.tablebuddy.common.entity.BaseTimeEntity;
import com.cs2404.tablebuddy.member.entity.DeleteStatus;
import com.cs2404.tablebuddy.member.entity.MemberEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StoreEntity extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "member_id")
    private MemberEntity member;

    @Column(name = "name")
    private String name;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "category")
    private Category category;

    @Column(name = "max_waiting_capacity")
    private Long maxWaitingCapacity;         // 최대 대기인원

    @Column(name = "is_deleted")
    private DeleteStatus isDeleted;

}
