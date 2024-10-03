package com.cs2404.tablebuddy.jongkuk;

import com.cs2404.tablebuddy.jongkuk.memberenum.DeleteStatus;
import com.cs2404.tablebuddy.jongkuk.memberenum.MemberRole;
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

    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @Column(name = "phone_number", length = 50, nullable = false)
    private String phoneNumber;

    @Column(name = "email", length = 50, nullable = false)
    private String email;

    @Column(name = "password", length = 30, nullable = false)
    private String password;

    @Column(name = "role", length = 20, nullable = false)
    private MemberRole role; // enum

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

}
