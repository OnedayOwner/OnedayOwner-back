package com.OnedayOwner.server.platform.user.dto.request;

import com.OnedayOwner.server.platform.user.entity.Gender;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class CustomerJoinRequest {

    private final String name;
    private final String phoneNumber;
    private final Gender gender;
    private final LocalDate birth;
    private final String password;
    private final String email;

    @Builder
    public CustomerJoinRequest(String name, String phoneNumber, Gender gender, LocalDate birth, String password, String email) {
        this.name = name;
        this.phoneNumber=phoneNumber;
        this.gender=gender;
        this.birth=birth;
        this.password=password;
        this.email=email;
    }
}
