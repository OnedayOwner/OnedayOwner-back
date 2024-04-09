package com.OnedayOwner.server.platform.user.entity;

import com.OnedayOwner.server.global.model.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Customer extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private Long id;

    private String name;
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private LocalDate birth;
    private int point;
    private String email;
    private String password;

    @Builder
    public Customer(String name, String phoneNumber, Gender gender, LocalDate birth, String email, String password) {
        this.name = name;
        this.phoneNumber=phoneNumber;
        this.gender=gender;
        this.birth=birth;
        this.email=email;
        this.password=password;
        this.point=0;
    }
}
