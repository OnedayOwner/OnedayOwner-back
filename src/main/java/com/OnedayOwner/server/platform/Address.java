package com.OnedayOwner.server.platform;

import com.OnedayOwner.server.platform.user.dto.UserDto;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Address {

    private String zipcode;
    private String street;
    private String detail;


    @Builder
    public Address(String street, String zipcode, String detail) {
        this.street = street;
        this.zipcode = zipcode;
        this.detail = detail;
    }

    public Address(UserDto.AddressForm addressForm){
        this.zipcode = addressForm.getZipcode();
        this.street = addressForm.getStreet();
        this.detail = addressForm.getDetail();
    }
}
