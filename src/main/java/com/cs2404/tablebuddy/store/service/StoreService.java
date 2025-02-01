package com.cs2404.tablebuddy.store.service;


import com.cs2404.tablebuddy.common.exception.CustomBusinessException;
import com.cs2404.tablebuddy.common.exception.ErrorCode;
import com.cs2404.tablebuddy.member.dto.MemberDto;
import com.cs2404.tablebuddy.member.entity.MemberEntity;
import com.cs2404.tablebuddy.member.entity.MemberRole;
import com.cs2404.tablebuddy.member.repository.MemberRepository;
import com.cs2404.tablebuddy.store.dto.SaveBusinessHourDto;
import com.cs2404.tablebuddy.store.dto.SaveStoreDto;
import com.cs2404.tablebuddy.store.dto.UpdateStoreDto;
import com.cs2404.tablebuddy.store.entity.BusinessDay;
import com.cs2404.tablebuddy.store.entity.BusinessHourEntity;
import com.cs2404.tablebuddy.store.entity.DeleteStatus;
import com.cs2404.tablebuddy.store.entity.StoreEntity;
import com.cs2404.tablebuddy.store.repository.StoreRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoreService {

    private final StoreRepository storeRepository;
    private final MemberRepository memberRepository;

    @Transactional
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

    @Transactional
    public Long saveStore(SaveStoreDto.Request createStoreRequest, MemberDto loginMember) {

        MemberEntity member = getMemberEntity(loginMember);

        // 오너 권한을 가지고 잊지 않을 경우 예외 발생
        if (member.getRole() != MemberRole.OWNER) {
            throw new CustomBusinessException(ErrorCode.INVALID_ROLE, "가게주인 권한을 가진 사용자만 가게 등록이 가능합니다.");
        }

        // 이미 등록한 가게가 있을 경우 예외 발생
        storeRepository.findByMember(member)
                .ifPresent(store -> {
                    throw new CustomBusinessException(ErrorCode.ALREADY_EXIST_STORE);
                });

        StoreEntity storeEntity = StoreEntity.builder()
                .member(member)
                .name(createStoreRequest.getName())
                .category(createStoreRequest.getCategory())
                .maxWaitingCapacity(createStoreRequest.getMaxWaitingCapacity())
                .isDeleted(DeleteStatus.N)
                .build();


        Long storeId = storeRepository.saveStore(storeEntity);

        return storeId;
    }

    public Long updateStore(UpdateStoreDto.Request updateStoreRequest, MemberDto loginMember, Long storeId) {
        MemberEntity memberEntity = getMemberEntity(loginMember);

        StoreEntity storeEntity = storeRepository.findById(storeId).orElseThrow(() -> new CustomBusinessException(ErrorCode.STORE_NOT_FOUND));

        if (!isValidMember(memberEntity, storeEntity)) {
            throw new CustomBusinessException(ErrorCode.STORE_OWNER_MISMATCH);
        }

        storeEntity.updateStore(updateStoreRequest);

        return storeEntity.getId();
    }

    private boolean isValidMember(MemberEntity member, StoreEntity store) {
        return member.getId().equals(store.getMember().getId());
    }

    private MemberEntity getMemberEntity(MemberDto loginMember) {
        return memberRepository.findMemberByMemberId(loginMember.getId())
                .orElseThrow(() -> new CustomBusinessException(ErrorCode.MEMBER_NOT_FOUND));
    }

}
