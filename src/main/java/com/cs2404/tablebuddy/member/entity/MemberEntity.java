package com.cs2404.tablebuddy.member.entity;

import com.cs2404.tablebuddy.common.entity.BaseTimeEntity;
import com.cs2404.tablebuddy.common.entity.DeleteStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MemberEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(name = "name", length = 30, nullable = false)
    private String name;

    @Column(name = "phone_number", length = 50, nullable = false, unique = true)
    private String phoneNumber;

    @Column(name = "email", length = 30, nullable = false, unique = true)
    private String email;

    @Column(name = "password", length = 70, nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", length = 20, nullable = false)
    private MemberRole role; // enum

    @Enumerated(EnumType.STRING)
    @Column(name = "is_deleted", length = 1, nullable = false)
    private DeleteStatus isDeleted;


    @Builder
    public MemberEntity(Long id, String name, String phoneNumber, String email, String password, MemberRole role, DeleteStatus isDeleted) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
        this.role = role;
        this.isDeleted = isDeleted;
    }

    public boolean isOwner() {
        return role == MemberRole.OWNER;
    }
    public boolean isCustomer() {
        return role == MemberRole.CUSTOMER;
    }
}
