package com.OnedayOwner.server.platform.reservation.dto;

import com.OnedayOwner.server.platform.Address;
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
import java.time.LocalTime;
import java.util.List;

public class ReservationDto {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class ReservationForm{
        private LocalDateTime reservationTime;
        private int numberOfPeople;
        private Long popupId;
        private List<ReservationMenuForm> reservationMenus;

        @Builder
        public ReservationForm (LocalDateTime reservationTime, int numberOfPeople, Long popupId,
                                List<ReservationMenuForm> reservationMenus
        ){
            this.reservationTime = reservationTime;
            this.numberOfPeople = numberOfPeople;
            this.popupId = popupId;
            this.reservationMenus = reservationMenus;
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
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
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class ReservationTimesDto {
        private List<ReservationTimeDto> reservationTimes;

        @Builder
        public ReservationTimesDto(List<ReservationTime> reservationTimes) {
            this.reservationTimes = reservationTimes.stream()
                    .map(ReservationTimeDto::new)
                    .toList();
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

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class ReservationDetailForCustomer{
        private Long reservationId;
        private LocalDateTime reservationTime;
        private int numberOfPeople;
        private PopupDto.PopupSummaryForReservation popupSummaryForReservation;
        private List<ReservationMenuDetail> reservationMenuDetails;
        @Builder
        public ReservationDetailForCustomer(Reservation reservation, PopupRestaurant popupRestaurant, Address address, List<ReservationMenuDetail> reservationMenuDetails) {
            this.reservationId = reservation.getId();
            this.reservationTime = reservation.getReservationTime();
            this.numberOfPeople = reservation.getNumberOfPeople();
            this.popupSummaryForReservation = PopupDto.PopupSummaryForReservation.builder()
                    .popupRestaurant(popupRestaurant)
                    .address(address)
                    .build();
            this.reservationMenuDetails = reservationMenuDetails;
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class ReservationMenuDetail{
        private int quantity;
        private String menuName;

        @Builder
        public ReservationMenuDetail(ReservationMenu reservationMenu, Menu menu) {
            this.quantity = reservationMenu.getQuantity();
            this.menuName = menu.getName();
        }
    }
}
