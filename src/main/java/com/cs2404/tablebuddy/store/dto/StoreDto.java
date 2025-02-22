package com.cs2404.tablebuddy.store.dto;

import com.cs2404.tablebuddy.store.entity.Category;
import com.cs2404.tablebuddy.store.entity.StoreEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreDto {

    private Long id;
    private Long memberId;
    private String memberName;
    private String name;
    private Category category;
    private Long maxWaitingCapacity;

    public StoreDto(StoreEntity storeEntity) {
        this.id = storeEntity.getId();
        this.memberId = storeEntity.getMember().getId();
        this.memberName = storeEntity.getMember().getName();
        this.name = storeEntity.getName();
        this.category = storeEntity.getCategory();
        this.maxWaitingCapacity = storeEntity.getMaxWaitingCapacity();
    }


}
