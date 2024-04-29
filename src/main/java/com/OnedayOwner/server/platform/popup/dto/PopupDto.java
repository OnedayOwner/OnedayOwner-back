package com.OnedayOwner.server.platform.popup.dto;

import com.OnedayOwner.server.platform.popup.entity.BusinessTime;
import com.OnedayOwner.server.platform.popup.entity.Menu;
import com.OnedayOwner.server.platform.popup.entity.PopupRestaurant;
import com.OnedayOwner.server.platform.reservation.entity.ReservationTime;
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
        private String name;
        private AddressForm address;
        private String description;


        public PopupSummary(PopupRestaurant popupRestaurant) {
            this.id = popupRestaurant.getId();
            name = popupRestaurant.getName();
            this.address = AddressForm.builder()
                    .city(popupRestaurant.getAddress().getCity())
                    .street(popupRestaurant.getAddress().getStreet())
                    .zipcode(popupRestaurant.getAddress().getZipcode())
                    .detail(popupRestaurant.getAddress().getDetail())
                    .build();
            this.description = popupRestaurant.getDescription();
        }
    }

    @Getter
    @NoArgsConstructor
    public static class PopupHistoryDetail extends PopupSummary{
        private List<MenuDetail> menus;

        @Builder
        public PopupHistoryDetail(PopupRestaurant popupRestaurant) {
            super(popupRestaurant);
            this.menus = popupRestaurant.getMenus().stream()
                    .map(MenuDetail::new)
                    .toList();
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class PopupInBusinessDetail extends PopupSummary{
        private List<ReservationTimeDto> reservationTimes;
        private List<MenuDetail> menus;
        private List<BusinessTimeDto> businessTimes;

        @Builder
        public PopupInBusinessDetail(PopupRestaurant popupRestaurant) {
            super(popupRestaurant);
            this.reservationTimes = popupRestaurant.getReservationTimes().stream()
                    .map(ReservationTimeDto::new)
                    .toList();

            this.menus = popupRestaurant.getMenus().stream()
                    .map(MenuDetail::new)
                    .toList();
            this.businessTimes = popupRestaurant.getBusinessTimes().stream()
                    .map(BusinessTimeDto::new)
                    .toList();
        }
    }

    @Getter
    @NoArgsConstructor
    public static class MenuSummary{
        private Long id;
        private String name;
        private int price;
        private String imageUrl;


        public MenuSummary(Menu menu) {
            this.id = menu.getId();
            this.name = menu.getName();
            this.price = menu.getPrice();
            this.imageUrl = menu.getImageUrl();
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class MenuDetail extends MenuSummary{
        private String description;

        @Builder
        public MenuDetail(Menu menu) {
            super(menu);
            this.description = menu.getDescription();
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class BusinessTimeDto{
        private Long id;
        private LocalTime openTime;
        private LocalTime closeTime;

        @Builder
        public BusinessTimeDto(BusinessTime businessTime) {
            this.id = businessTime.getId();
            this.openTime = businessTime.getOpenTime();
            this.closeTime = businessTime.getCloseTime();
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class ReservationTimeDto{
        private Long id;
        private LocalTime startTime;
        private LocalTime endTime;
        private int maxPeople;

        @Builder
        public ReservationTimeDto(ReservationTime reservationTime) {
            this.id = reservationTime.getId();
            this.startTime = reservationTime.getStartTime();
            this.endTime = reservationTime.getEndTime();
            this.maxPeople = reservationTime.getMaxPeople();
        }
    }

}