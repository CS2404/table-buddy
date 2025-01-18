package com.cs2404.tablebuddy.store.service;


import com.cs2404.tablebuddy.store.dto.SaveBusinessHourDto;
import com.cs2404.tablebuddy.store.entity.BusinessDay;
import com.cs2404.tablebuddy.store.entity.BusinessHourEntity;
import com.cs2404.tablebuddy.store.entity.StoreEntity;
import com.cs2404.tablebuddy.store.repository.StoreRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;

    @Transactional
    public void saveBusinessHour(Long storeId, SaveBusinessHourDto.Request saveBusinessHourRequest) {

        StoreEntity foundStore = storeRepository.findById(storeId).orElseThrow();

        for (BusinessDay openDay : saveBusinessHourRequest.getOpenDays()) {
            BusinessHourEntity newBusinessHourEntity = BusinessHourEntity.builder()
                    .store(foundStore)
                    .startTime(saveBusinessHourRequest.getStartTime())
                    .endTime(saveBusinessHourRequest.getEndTime())
                    .openDay(openDay)
                    .build();

            storeRepository.saveBusinessHour(newBusinessHourEntity);
        }
    }
}
