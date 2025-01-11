package com.cs2404.tablebuddy.store.controller;


import com.cs2404.tablebuddy.common.config.security.LoginMember;
import com.cs2404.tablebuddy.member.dto.MemberDto;
import com.cs2404.tablebuddy.store.dto.CreateStoreDto;
import com.cs2404.tablebuddy.store.dto.SaveBusinessHourDto;
import com.cs2404.tablebuddy.store.service.StoreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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

  @PostMapping
  public ResponseEntity<CreateStoreDto.Response> createStore(
      @Valid @RequestBody CreateStoreDto.Request createStoreRequest,
      @LoginMember MemberDto loginMember
  ) {
    Long storeId = storeService.createStore(createStoreRequest, loginMember);

    return ResponseEntity.ok(new CreateStoreDto.Response(storeId));
  }

}
