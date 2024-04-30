package com.OnedayOwner.server.platform;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Address {

    private String city;
    private String street;
    private String zipcode;
    private String detail;


    @Builder
    public Address(String city, String street, String zipcode, String detail) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
        this.detail = detail;
    }
}
