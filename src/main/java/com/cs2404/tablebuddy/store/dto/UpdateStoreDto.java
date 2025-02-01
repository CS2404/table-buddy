package com.cs2404.tablebuddy.store.dto;

import com.cs2404.tablebuddy.store.entity.Category;
import com.cs2404.tablebuddy.store.entity.DeleteStatus;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UpdateStoreDto {
    @Getter
    @NoArgsConstructor
    public static class Request {

        @Size(max = 20, message = "가게 이름은 최대 20자까지 가능합니다.")
        private String name;
        private Category category;
        private Long maxWaitingCapacity;
        private DeleteStatus deleteStatus;

        @Builder
        public Request(String name,
                       Category category,
                       Long maxWaitingCapacity,
                       DeleteStatus deleteStatus) {
            this.name = name;
            this.category = category;
            this.maxWaitingCapacity = maxWaitingCapacity;
            this.deleteStatus = deleteStatus;
        }

    }


    @Getter
    @NoArgsConstructor
    public static class Response {

        private Long id;

        public Response(Long id) {
            this.id = id;
        }

    }
}
