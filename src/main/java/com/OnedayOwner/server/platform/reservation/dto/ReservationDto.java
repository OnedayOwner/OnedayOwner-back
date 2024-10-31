package com.OnedayOwner.server.platform.reservation.dto;

import com.OnedayOwner.server.platform.popup.dto.PopupDto;
import com.OnedayOwner.server.platform.popup.entity.Menu;
import com.OnedayOwner.server.platform.popup.entity.PopupRestaurant;
import com.OnedayOwner.server.platform.reservation.entity.Reservation;
import com.OnedayOwner.server.platform.reservation.entity.ReservationMenu;
import com.OnedayOwner.server.platform.reservation.entity.ReservationTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static lombok.AccessLevel.PROTECTED;

public class ReservationDto {

    @Getter
    @NoArgsConstructor(access = PROTECTED)
    public static class ReservationForm{
        private Long popupId;
        private Long reservationTimeId;
        private int numberOfPeople;
        private List<ReservationMenuForm> reservationMenus;

        @Builder
        public ReservationForm (Long reservationTimeId, int numberOfPeople, Long popupId,
                                List<ReservationMenuForm> reservationMenus
        ){
            this.popupId = popupId;
            this.reservationTimeId = reservationTimeId;
            this.numberOfPeople = numberOfPeople;
            this.reservationMenus = reservationMenus;
        }
    }

    @Getter
    @NoArgsConstructor(access = PROTECTED)
    public static class ReservationMenuForm{
        private int quantity;
        private Long menuId;
        @Builder
        public ReservationMenuForm(int quantity, Long menuId) {
            this.quantity = quantity;
            this.menuId = menuId;
        }
    }

    @Getter
    @NoArgsConstructor(access = PROTECTED)
    public static class ReservationInfoDto{
        private Long popupId;
        private String popupName;
        private List<PopupDto.ReservationTimeDto> reservationTimes;
        @Builder
        public ReservationInfoDto(PopupRestaurant popupRestaurant, List<ReservationTime> reservationTimes) {
            this.popupId = popupRestaurant.getId();
            this.popupName = popupRestaurant.getName();
            this.reservationTimes = reservationTimes.stream()
                    .map(PopupDto.ReservationTimeDto::new)
                    .toList();
        }
    }

    @Getter
    @NoArgsConstructor(access = PROTECTED)
    public static class ReservationMenuDto{
        private Long popupId;
        private String popupName;
        private List<PopupDto.MenuDetail> menus;
        @Builder
        public ReservationMenuDto(PopupRestaurant popupRestaurant) {
            this.popupId = popupRestaurant.getId();
            this.popupName = popupRestaurant.getName();
            this.menus = popupRestaurant.getMenus()
                    .stream()
                    .map(PopupDto.MenuDetail::new)
                    .toList();
        }
    }

    @Getter
    @NoArgsConstructor(access = PROTECTED)
    public static class ReservationDetail {
        private Long id;
        private LocalDateTime reservationDateTime;
        private int numberOfPeople;
        private PopupDto.PopupSummaryForReservation popupSummaryForReservation;
        private List<ReservationMenuDetail> reservationMenuDetails;
        @Builder
        public ReservationDetail(Reservation reservation) {
            this.id = reservation.getId();
            this.reservationDateTime = reservation.getReservationDateTime();
            this.numberOfPeople = reservation.getNumberOfPeople();
            this.popupSummaryForReservation = PopupDto.PopupSummaryForReservation.builder()
                    .popupRestaurant(reservation.getPopupRestaurant())
                    .build();
            this.reservationMenuDetails = reservation.getReservationMenus().stream()
                    .map(ReservationMenuDetail::new)
                    .toList();
        }
    }

    @Getter
    @NoArgsConstructor(access = PROTECTED)
    public static class ReservationDetailForUser extends ReservationDetail {
        private int totalPrice;
        private Boolean isFeedbackEnabled;

        public ReservationDetailForUser(Reservation reservation, Long feedbackId) {
            super(reservation);
            this.totalPrice = 0;
            reservation.getReservationMenus().forEach(reservationMenu -> {
                totalPrice += reservationMenu.getMenu().getPrice() * reservationMenu.getQuantity();
            });
            this.isFeedbackEnabled = LocalDateTime.now().isAfter(reservation.getReservationDateTime()) && feedbackId != null;
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class ReservationMenuDetail{
        private Long id;
        private int quantity;
        private String name;
        private int price;
        private String imageUrl;

        @Builder
        public ReservationMenuDetail(ReservationMenu reservationMenu) {
            this.id = reservationMenu.getId();
            this.quantity = reservationMenu.getQuantity();
            this.name = reservationMenu.getMenu().getName();
            this.price = reservationMenu.getMenu().getPrice();
            this.imageUrl = reservationMenu.getMenu().getImageUrl();
        }
    }

    @Getter
    @NoArgsConstructor(access = PROTECTED)
    public static class ReservationSummary{
        private Long id;
        private int numberOfPeople;
        private String popupName;
        private PopupDto.AddressForm address;
        private LocalDateTime reservationDateTime;
        private String menuImageUrl;

        public ReservationSummary(Reservation reservation) {
            this.id = reservation.getId();
            this.numberOfPeople = reservation.getNumberOfPeople();
            this.popupName = reservation.getPopupRestaurant().getName();
            this.address = PopupDto.AddressForm.builder()
                    .street(reservation.getPopupRestaurant().getAddress().getStreet())
                    .zipcode(reservation.getPopupRestaurant().getAddress().getZipcode())
                    .detail(reservation.getPopupRestaurant().getAddress().getDetail())
                    .build();
            this.reservationDateTime = reservation.getReservationDateTime();
            this.menuImageUrl = reservation.getPopupRestaurant().getMenus().stream().findFirst()
                    .map(Menu::getImageUrl)
                    .orElse(null);
        }
    }
}
