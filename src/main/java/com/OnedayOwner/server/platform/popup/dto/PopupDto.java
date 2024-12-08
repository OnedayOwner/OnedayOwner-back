package com.OnedayOwner.server.platform.popup.dto;

import com.OnedayOwner.server.platform.Address;
import com.OnedayOwner.server.platform.popup.entity.BusinessTime;
import com.OnedayOwner.server.platform.popup.entity.Menu;
import com.OnedayOwner.server.platform.popup.entity.PopupRestaurant;
import com.OnedayOwner.server.platform.reservation.entity.ReservationTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class PopupDto {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class PopupRestaurantForm {
        private String name;
        private LocalDateTime startDateTime;
        private LocalDateTime endDateTime;
        private String description;
        private AddressForm address;
        private List<BusinessTimeForm> businessTimes;
        private List<MenuForm> menuForms;

        @Builder
        public PopupRestaurantForm(String name, LocalDateTime startDateTime, LocalDateTime endDateTime, String description,
                                   AddressForm address, List<BusinessTimeForm> businessTimes, List<MenuForm> menuForms
        ) {
            this.name = name;
            this.startDateTime = startDateTime;
            this.endDateTime = endDateTime;
            this.description = description;
            this.address = address;
            this.businessTimes = businessTimes;
            this.menuForms = menuForms;
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class BusinessTimeForm {
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
    public static class AddressForm {
        private String street;
        private String zipcode;
        private String detail;

        @Builder
        public AddressForm(String street, String zipcode, String detail) {
            this.street = street;
            this.zipcode = zipcode;
            this.detail = detail;
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class MenuForm {
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
    public static class PopupSummary {
        private Long id;
        private String name;
        private AddressForm address;
        private String description;
        private List<MenuDetail> menus;
        private LocalDateTime startDateTime;
        private LocalDateTime endDateTime;
        private String imageUrl;


        public PopupSummary(PopupRestaurant popupRestaurant) {
            this.id = popupRestaurant.getId();
            this.name = popupRestaurant.getName();
            this.address = AddressForm.builder()
                    .street(popupRestaurant.getAddress().getStreet())
                    .zipcode(popupRestaurant.getAddress().getZipcode())
                    .detail(popupRestaurant.getAddress().getDetail())
                    .build();
            this.description = popupRestaurant.getDescription();
            this.startDateTime = popupRestaurant.getStartDateTime();
            this.endDateTime = popupRestaurant.getEndDateTime();

            this.menus = popupRestaurant.getMenus().stream()
                    .map(MenuDetail::new)
                    .toList();
            this.imageUrl = popupRestaurant.getImageUrl();
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class PopupHistoryDetail extends PopupSummary {
        private List<BusinessTimeDto> businessTimes;
        private Long totalReservation;
        private Long totalReservationPeople;
        private Long totalFeedback;
        @Builder
        public PopupHistoryDetail(PopupRestaurant popupRestaurant,
                                  Long totalReservation,
                                  Long totalReservationPeople,
                                  Long totalFeedback) {
            super(popupRestaurant);
            this.businessTimes = popupRestaurant.getBusinessTimes().stream()
                    .map(BusinessTimeDto::new)
                    .toList();
            this.totalReservation = totalReservation;
            this.totalReservationPeople = totalReservationPeople;
            this.totalFeedback = totalFeedback;
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class PopupInBusinessDetail extends PopupSummary {
        private List<ReservationTimeDto> reservationTimes;
        private List<BusinessTimeDto> businessTimes;
        private Long totalReservation;
        private Long totalReservationPeople;

        @Builder
        public PopupInBusinessDetail(PopupRestaurant popupRestaurant
                , Long totalReservation, Long totalReservationPeople) {
            super(popupRestaurant);
            this.reservationTimes = popupRestaurant.getReservationTimes().stream()
                    .map(ReservationTimeDto::new)
                    .toList();

            this.businessTimes = popupRestaurant.getBusinessTimes().stream()
                    .map(BusinessTimeDto::new)
                    .toList();
            this.totalReservation = totalReservation;
            this.totalReservationPeople = totalReservationPeople;
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class MenuSummary {
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
    public static class MenuDetail extends MenuSummary {
        private String description;

        @Builder
        public MenuDetail(Menu menu) {
            super(menu);
            this.description = menu.getDescription();
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class BusinessTimeDto {
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
    public static class ReservationTimeDto {
        private Long id;
        private LocalDate reservationDate;
        private LocalTime startTime;
        private LocalTime endTime;
        private int maxPeople;

        @Builder
        public ReservationTimeDto(ReservationTime reservationTime) {
            this.id = reservationTime.getId();
            this.reservationDate = reservationTime.getReservationDate();
            this.startTime = reservationTime.getStartTime();
            this.endTime = reservationTime.getEndTime();
            this.maxPeople = reservationTime.getMaxPeople();
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class PopupSummaryForReservation {
        private Long id;
        private String name;
        private AddressForm address;

        @Builder
        public PopupSummaryForReservation(PopupRestaurant popupRestaurant) {
            this.id = popupRestaurant.getId();
            this.name = popupRestaurant.getName();
            this.address = AddressForm.builder()
                    .street(popupRestaurant.getAddress().getStreet())
                    .zipcode(popupRestaurant.getAddress().getZipcode())
                    .detail(popupRestaurant.getAddress().getDetail())
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class PopupSummaryForCustomer {
        private Long id;
        private String name;
        private AddressForm address;
        private String description;
        private LocalDateTime startDateTime;
        private LocalDateTime endDateTime;
        private Boolean inBusiness;
        private String imageUrl;

        public PopupSummaryForCustomer(PopupRestaurant popupRestaurant) {
            this.id = popupRestaurant.getId();
            this.name = popupRestaurant.getName();
            this.address = AddressForm.builder()
                    .street(popupRestaurant.getAddress().getStreet())
                    .zipcode(popupRestaurant.getAddress().getZipcode())
                    .detail(popupRestaurant.getAddress().getDetail())
                    .build();
            this.description = popupRestaurant.getDescription();
            this.startDateTime = popupRestaurant.getStartDateTime();
            this.endDateTime = popupRestaurant.getEndDateTime();
            this.inBusiness = popupRestaurant.getInBusiness();
            this.imageUrl = popupRestaurant.getImageUrl();
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class PopupDetailForCustomer extends PopupSummary{
        private List<BusinessTimeDto> businessTimes;

        @Builder
        public PopupDetailForCustomer(PopupRestaurant popupRestaurant) {
            super(popupRestaurant);
            this.businessTimes = popupRestaurant.getBusinessTimes().stream()
                    .map(BusinessTimeDto::new)
                    .toList();
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class ReservationInfoForOwnerSummary{
        private LocalDate date;
        private long totalReservation;
        private long totalPeople;

        public ReservationInfoForOwnerSummary(LocalDate date, long totalReservation, long totalPeople) {
            this.date = date;
            this.totalReservation = totalReservation;
            this.totalPeople = totalPeople;
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class ReservationMenuCount{
        private LocalDateTime reservationTime;
        private String menuName;
        private int quantity;

        public ReservationMenuCount(LocalDateTime reservationTime, String menuName, int quantity) {
            this.reservationTime = reservationTime;
            this.menuName = menuName;
            this.quantity = quantity;
        }
    }
}
