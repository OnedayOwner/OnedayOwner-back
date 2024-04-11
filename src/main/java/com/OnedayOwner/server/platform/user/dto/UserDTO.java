package com.OnedayOwner.server.platform.user.dto;

import com.OnedayOwner.server.platform.user.entity.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


public class UserDTO {
    /*
    Request
     */
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Builder
    public static class UserJoinRequest{
        private String name;
        private String phoneNumber;
        private Gender gender;
        private LocalDate birth;
        private String password;
        private String email;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Builder
    public static class UserLoginRequest{
        private String email;
        private String password;
    }

    /*
    Response
     */
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Builder
    public static class UserLoginResponse{
        private Long id;
        private String name;
        private int point;
    }
}
