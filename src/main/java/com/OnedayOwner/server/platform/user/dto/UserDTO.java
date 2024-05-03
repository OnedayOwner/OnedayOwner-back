package com.OnedayOwner.server.platform.user.dto;

import com.OnedayOwner.server.platform.user.entity.Gender;
import com.OnedayOwner.server.platform.user.entity.Owner;
import lombok.*;

import java.time.LocalDate;


public class UserDTO {

    /*
    Request
     */
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @NoArgsConstructor(access = AccessLevel.PROTECTED)
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
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Getter
    @Builder
    public static class UserLoginResponse{
        private Long id;
        private String name;
        private int point;
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class OwnerLonginResponse{
        private Long id;

        private String name;
        private String phoneNumber;

        private Gender gender;

        private LocalDate birth;
        private String email;

        @Builder
        public OwnerLonginResponse(Owner owner){
            this.id = owner.getId();
            this.name = owner.getName();
            this.phoneNumber = owner.getPhoneNumber();
            this.gender = owner.getGender();
            this.birth = owner.getBirth();
            this.email = owner.getEmail();
        }
    }
}
