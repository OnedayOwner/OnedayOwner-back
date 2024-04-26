package com.OnedayOwner.server.platform.popup.dto;

import com.OnedayOwner.server.platform.Address;
import com.OnedayOwner.server.platform.user.entity.Owner;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class PopupDto {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class PopupRestaurantForm{
        private String name;
        private LocalDateTime startDateTime;
        private LocalDateTime endDateTime;
        private String description;
        private List<BusinessTimeForm> businessTimes;

        @Builder
        public PopupRestaurantForm(String name, LocalDateTime startDateTime, LocalDateTime endDateTime, String description, List<BusinessTimeForm> businessTimes) {
            this.name = name;
            this.startDateTime = startDateTime;
            this.endDateTime = endDateTime;
            this.description = description;
            this.businessTimes = businessTimes;

        }
    }

    @Getter
    @NoArgsConstructor
    public static class BusinessTimeForm{
        private LocalTime openTime;
        private LocalTime closeTime;
        private long reservationTimeUnit;
        private int maxPeoplePerTime;

        @Builder
        public BusinessTimeForm(LocalTime openTime, LocalTime closeTime, int reservationTimeUnit, int maxPeoplePerTime) {
            this.openTime = openTime;
            this.closeTime = closeTime;
            this.reservationTimeUnit = reservationTimeUnit;
            this.maxPeoplePerTime = maxPeoplePerTime;
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class AddressForm{
        private String city;
        private String street;
        private String zipcode;
        private String detail;

        @Builder
        public AddressForm(String city, String street, String zipcode, String detail) {
            this.city = city;
            this.street = street;
            this.zipcode = zipcode;
            this.detail = detail;
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class MenuForm{
        private String name;
        private int price;
        private String description;

        @Builder
        public MenuForm(String name, int price, String description) {
            this.name = name;
            this.price = price;
            this.description = description;
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class PopupSummary{
        private Long id;
        private String Name;
        private AddressForm address;
        private String description;

        @Builder
        public PopupSummary(Long id, String name, Address address, String description) {
            this.id = id;
            Name = name;
            this.address = AddressForm.builder()
                    .city(address.getCity())
                    .street(address.getStreet())
                    .zipcode(address.getZipcode())
                    .detail(address.getDetail())
                    .build();
            this.description = description;
        }

    }

}
