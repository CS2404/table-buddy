package com.cs2404.tablebuddy.store.controller;


import com.cs2404.tablebuddy.common.config.security.LoginMember;
import com.cs2404.tablebuddy.member.dto.MemberDto;
import com.cs2404.tablebuddy.reservation.dto.ReservationAddDto;
import com.cs2404.tablebuddy.store.dto.SaveBusinessHourDto;
import com.cs2404.tablebuddy.store.dto.SaveStoreDto;
import com.cs2404.tablebuddy.store.dto.UpdateStoreDto;
import com.cs2404.tablebuddy.store.service.StoreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api-root-path}/v1/stores")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    // 가게 영업시간 등록 API
    @PostMapping("/{storeId}/business-hour")
    public ResponseEntity<Void> saveStoreInfo(
            @PathVariable Long storeId,
            @Valid @RequestBody SaveBusinessHourDto.Request saveBusinessHourRequest
    ) {
        storeService.saveBusinessHour(storeId, saveBusinessHourRequest);

        return ResponseEntity.ok().build();
    }

    // 가게 등록 API
    @PostMapping
    public ResponseEntity<SaveStoreDto.Response> saveStore(
            @Valid @RequestBody SaveStoreDto.Request saveStoreRequest,
            @LoginMember MemberDto loginMember
    ) {
        Long storeId = storeService.saveStore(saveStoreRequest, loginMember);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new SaveStoreDto.Response(storeId));
    }

    // 가게 수정 API
    @PutMapping("/{storeId}")
    public ResponseEntity<UpdateStoreDto.Response> updateStore(
            @PathVariable Long storeId,
            @Valid @RequestBody UpdateStoreDto.Request updateStoreRequest,
            @LoginMember MemberDto loginMember
    ) {
        Long storeId = storeService.updateStore(updateStoreRequest, loginMember, storeId);

        return ResponseEntity.ok(new UpdateStoreDto.Response(storeId));
    }
}
