package com.OnedayOwner.server.platform.user.dto;

import com.OnedayOwner.server.platform.Address;
import com.OnedayOwner.server.platform.user.entity.Gender;
import com.OnedayOwner.server.platform.user.entity.Owner;
import com.OnedayOwner.server.platform.user.entity.Role;
import com.OnedayOwner.server.platform.user.entity.User;
import lombok.*;

import java.time.LocalDate;

import static lombok.AccessLevel.PROTECTED;


public class UserDto {
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

    @Getter
    @NoArgsConstructor(access = PROTECTED)
    public static class JoinDto{
        private String name;
        private String phoneNumber;
        private LocalDate birth;
        private String loginId;
        private String password;
        private String email;

        private Gender gender;

        private AddressForm addressForm;

        private Long codeId;
        private String verificationCode;

        @Builder

        public JoinDto(String name, String phoneNumber, LocalDate birth,
                       String loginId, String password, String email, Role role, Gender gender, AddressForm addressForm,
                       Long codeId, String verificationCode) {
            this.name = name;
            this.phoneNumber = phoneNumber;
            this.birth = birth;
            this.loginId = loginId;
            this.password = password;
            this.email = email;
            this.gender = gender;
            this.addressForm = addressForm;
            this.codeId = codeId;
            this.verificationCode = verificationCode;
        }
    }

    @Getter
    @NoArgsConstructor(access = PROTECTED)
    public static class AddressForm{
        private String zipcode;
        private String street;
        private String detail;

        @Builder

        public AddressForm(String zipcode, String street, String detail) {
            this.zipcode = zipcode;
            this.street = street;
            this.detail = detail;
        }
    }

    @Getter
    @NoArgsConstructor(access = PROTECTED)
    public static class LoginDto{
        private String loginId;
        private String password;
    }

    @Getter
    @NoArgsConstructor(access = PROTECTED)
    public static class UserInfo{
        private String name;
        private String phoneNumber;
        private LocalDate birth;
        private String loginId;
        private String email;

        private Role role;
        private Gender gender;

        public UserInfo(User user) {
            this.name = user.getName();
            this.phoneNumber = user.getPhoneNumber();
            this.birth = user.getBirth();
            this.loginId = user.getEmail();
            this.email = user.getEmail();
            this.role = user.getRole();
            this.gender = user.getGender();
        }
    }

    @Getter
    @NoArgsConstructor(access = PROTECTED)
    public static class OwnerInfo extends UserInfo{
        private String name;
        private String phoneNumber;
        private LocalDate birth;
        private String loginId;
        private String email;

        private Role role;
        private Gender gender;

        @Builder
        public OwnerInfo(User owner){
            super(owner);
        }
    }

    @Getter
    @NoArgsConstructor(access = PROTECTED)
    public static class CustomerInfo extends UserInfo{

        private Address address;

        @Builder
        public CustomerInfo(User customer) {
            super(customer);
            this.address = customer.getAddress();
        }
    }
}
