package com.cs2404.tablebuddy.store.dto;

import com.cs2404.tablebuddy.store.entity.StoreEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Getter
@NoArgsConstructor
public class BusinessHourDto {
    private StoreEntity store;
    private LocalTime startTime;
    private LocalTime endTime;
    private String openDay;
}
