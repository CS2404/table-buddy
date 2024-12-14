package com.cs2404.tablebuddy.store.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SaveStoreInfoDto {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Request {
        private AddressDto storeAddressDto;
        private BusinessHourDto businessHourDto;

        @Builder
        public Request(AddressDto storeAddressDto,
                       BusinessHourDto businessHourDto) {
            this.storeAddressDto = storeAddressDto;
            this.businessHourDto = businessHourDto;
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
