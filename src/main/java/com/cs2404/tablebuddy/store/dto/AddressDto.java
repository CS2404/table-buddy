package com.cs2404.tablebuddy.store.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AddressDto {
    private long storeId;
    @NotEmpty
        /*
          ^(캐럿): 문자열 시작
          \\d: 숫자 digit 하나를 의미, 0~9의 값 중 하나
          {5}: 반복 횟수 지정 바로 앞의 \\d가 정확히 5번 반복되어야 함
          $: 문자열의 끝
         */
    @Pattern(regexp = "^\\d{5}$", message = "잘못된 우편번호입니다")
    private String zipcode;
    @NotEmpty
    private String country;
    @NotEmpty
    private String state;
    @NotEmpty
    private String city;
    private String district;
    @NotEmpty
    private String addressLine1;
    private String addressLine2;

    @Builder
    public AddressDto(Long storeId,
                      String zipcode,
                      String country,
                      String state,
                      String city,
                      String district,
                      String addressLine1,
                      String addressLine2) {
        this.storeId = storeId;
        this.zipcode = zipcode;
        this.country = country;
        this.state = state;
        this.city = city;
        this.district = district;
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
    }
}
