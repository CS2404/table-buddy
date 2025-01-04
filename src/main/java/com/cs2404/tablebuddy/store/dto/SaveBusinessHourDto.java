package com.cs2404.tablebuddy.store.dto;

import com.cs2404.tablebuddy.store.entity.BusinessDay;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalTime;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SaveBusinessHourDto {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Request {

        @DateTimeFormat(pattern = "HH:mm")
        private LocalTime startTime;
        @DateTimeFormat(pattern = "HH:mm")
        private LocalTime endTime;
        @NotNull
        private List<BusinessDay> openDays;

        @Builder
        public Request(LocalTime startTime,
                       LocalTime endTime,
                       List<String> openDays) {
            this.startTime = startTime;
            this.endTime = endTime;
            this.openDays = openDays.stream()
                    .map(BusinessDay::fromString)
                    .toList();
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
