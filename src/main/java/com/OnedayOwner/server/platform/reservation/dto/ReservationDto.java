package com.OnedayOwner.server.platform.reservation.dto;

import com.OnedayOwner.server.platform.popup.dto.PopupDto;
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
    public static class ReservationDetail {
        private Long Id;
        private LocalDateTime reservationDateTime;
        private int numberOfPeople;
        private PopupDto.PopupSummaryForReservation popupSummaryForReservation;
        private List<ReservationMenuDetail> reservationMenuDetails;
        @Builder
        public ReservationDetail(Reservation reservation) {
            this.Id = reservation.getId();
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
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class ReservationMenuDetail{
        private int quantity;
        private String menuName;

        @Builder
        public ReservationMenuDetail(ReservationMenu reservationMenu) {
            this.quantity = reservationMenu.getQuantity();
            this.menuName = reservationMenu.getMenu().getName();
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class ReservationSummary{
        private Long id;
        private int numberOfPeople;
        private String popupName;
        private LocalDateTime reservationDateTime;
        private List<ReservationMenuDetail> reservationMenuDetails;

        public ReservationSummary(Reservation reservation) {
            this.id = reservation.getId();
            this.numberOfPeople = reservation.getNumberOfPeople();
            this.popupName = reservation.getPopupRestaurant().getName();
            this.reservationDateTime = reservation.getReservationDateTime();
            this.reservationMenuDetails = reservation.getReservationMenus()
                    .stream()
                    .map(ReservationMenuDetail::new)
                    .toList();
        }
    }
}
