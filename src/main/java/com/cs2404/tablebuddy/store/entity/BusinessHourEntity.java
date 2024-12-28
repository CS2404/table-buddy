package com.cs2404.tablebuddy.store.entity;

import com.cs2404.tablebuddy.common.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Entity
@Getter
@Table(name = "business_hour")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BusinessHourEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "store_id")
    private StoreEntity store;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @Column(name = "open_day", nullable = false)
    private BusinessDay openDay;

    @Builder
    public BusinessHourEntity(Long id,
                              StoreEntity store,
                              LocalTime startTime,
                              LocalTime endTime,
                              BusinessDay openDay) {
        this.id = id;
        this.store = store;
        this.startTime = startTime;
        this.endTime = endTime;
        this.openDay = openDay;
    }
}
