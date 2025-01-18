package com.cs2404.tablebuddy.store.dto;

import com.cs2404.tablebuddy.store.entity.Category;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SaveStoreDto {

    @Getter
    @NoArgsConstructor
    public static class Request {

        @Size(max = 20, message = "가게 이름은 최대 20자까지 가능합니다.")
        private String name;
        @NotNull
        private Category category;
        @NotNull
        private Long maxWaitingCapacity;

        @Builder
        public Request(String name,
                       Category category,
                       Long maxWaitingCapacity) {
            this.name = name;
            this.category = category;
            this.maxWaitingCapacity = maxWaitingCapacity;
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