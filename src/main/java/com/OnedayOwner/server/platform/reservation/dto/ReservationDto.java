package com.OnedayOwner.server.platform.reservation.dto;

import com.OnedayOwner.server.platform.popup.dto.PopupDto;
import com.OnedayOwner.server.platform.popup.entity.Menu;
import com.OnedayOwner.server.platform.popup.entity.PopupRestaurant;
import com.OnedayOwner.server.platform.user.entity.Customer;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class ReservationDto {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class reservationForm{
        private LocalDateTime reservationTime;
        private int numberOfPeople;
        private PopupRestaurant popupRestaurant;
        private Customer customer;
        private List<ReservationMenuForm> reservationMenus;

        @Builder
        public reservationForm (LocalDateTime reservationTime, int numberOfPeople, PopupRestaurant popupRestaurant,
                                Customer customer, List<ReservationMenuForm> reservationMenus
        ){
            this.reservationTime = reservationTime;
            this.numberOfPeople = numberOfPeople;
            this.popupRestaurant = popupRestaurant;
            this.customer = customer;
            this.reservationMenus = reservationMenus;
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class ReservationMenuForm{
        private int quantity;
        private Menu menu;
        @Builder
        public ReservationMenuForm(int quantity, Menu menu) {
            this.quantity = quantity;
            this.menu = menu;
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class PopupDetailForReservationDto extends PopupDto.PopupSummary {
        private List<LocalDateTime> reservationPossibleDateTimes;

        @Builder
        public PopupDetailForReservationDto(PopupRestaurant popupRestaurant, List<LocalDateTime> reservationPossibleDateTimes) {
            super(popupRestaurant);
            this.reservationPossibleDateTimes = reservationPossibleDateTimes;
        }
    }
}
