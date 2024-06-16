package com.OnedayOwner.server.platform.user.entity;

import com.OnedayOwner.server.global.model.BaseTimeEntity;
import com.OnedayOwner.server.platform.Address;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"loginId", "role"})}
)
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    private String name;
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false)
    private Gender gender;


    private LocalDate birth;
    private String loginId;
    private String email;
    private String password;

    private Address address;

    private int point;

    @Builder

    public User(String name, String phoneNumber, Role role, Gender gender, LocalDate birth, String loginId, String email, String password, Address address, int point) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.gender = gender;
        this.birth = birth;
        this.loginId = loginId;
        this.email = email;
        this.password = password;
        this.address = address;
        this.point = point;
    }
}
