package com.cs2404.tablebuddy.member.dto;

import com.cs2404.tablebuddy.member.entity.DeleteStatus;
import com.cs2404.tablebuddy.member.entity.MemberEntity;
import com.cs2404.tablebuddy.member.entity.MemberRole;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberDto {

    private Long id;
    private DeleteStatus isDeleted;
    private MemberRole role;
    private String name;
    private String phoneNumber;

    private String email;
    private String password;

    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    @Builder
    public MemberDto(Long id, DeleteStatus isDeleted, MemberRole role, String name, String phoneNumber, String email, String password, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.id = id;
        this.isDeleted = isDeleted;
        this.role = role;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }


    public MemberDto(MemberEntity memberEntity) {
        this.id = memberEntity.getId();
        this.isDeleted = memberEntity.getIsDeleted();
        this.role = memberEntity.getRole();
        this.name = memberEntity.getName();
        this.phoneNumber = memberEntity.getPhoneNumber();

        this.email = memberEntity.getEmail();
        this.password = memberEntity.getPassword();

        this.createdAt = memberEntity.getCreatedAt();
        this.modifiedAt = memberEntity.getModifiedAt();
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority(role.toRoleString()));
        return roles;
    }

    public boolean isOwner() {
        return role == MemberRole.OWNER;
    }
    public boolean isCustomer() {
        return role == MemberRole.CUSTOMER;
    }
}
