package com.cs2404.tablebuddy.store.dto;

import com.cs2404.tablebuddy.store.entity.Category;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SaveStoreDto {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Request {
        @NotNull
        private String name;
        @NotNull
        private Category category;
        @NotNull
        @Positive
        private Long maxWaitingCapacity;

        @Builder
        public Request(String name, Category category, Long maxWaitingCapacity) {
            this.name = name;
            this.category = category;
            this.maxWaitingCapacity = maxWaitingCapacity;
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Response {
        private Long id;

        public Response(Long id) {
            this.id = id;
        }
    }
}
