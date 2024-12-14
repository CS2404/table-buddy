package com.cs2404.tablebuddy.store.service;

import com.cs2404.tablebuddy.member.entity.DeleteStatus;
import com.cs2404.tablebuddy.store.dto.SaveBusinessHourDto;
import com.cs2404.tablebuddy.store.dto.SaveStoreDto;
import com.cs2404.tablebuddy.store.entity.BusinessDay;
import com.cs2404.tablebuddy.store.entity.BusinessHourEntity;
import com.cs2404.tablebuddy.store.entity.StoreEntity;
import com.cs2404.tablebuddy.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;

    public Long saveStore(SaveStoreDto.Request saveStoreRequest) {

        // TODO: memberId 추가해야함
        StoreEntity newStore = StoreEntity.builder()
                .name(saveStoreRequest.getName())
                .category(saveStoreRequest.getCategory())
                .maxWaitingCapacity(saveStoreRequest.getMaxWaitingCapacity())
                .isDeleted(DeleteStatus.N)
                .build();

        return storeRepository.saveStore(newStore);
    }

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
