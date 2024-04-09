package com.OnedayOwner.server.platform.popup.entity;

import com.OnedayOwner.server.platform.feedback.entity.Feedback;
import com.OnedayOwner.server.platform.place.entity.PlaceInfo;
import com.OnedayOwner.server.platform.user.entity.Customer;
import com.OnedayOwner.server.platform.user.entity.Owner;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Reservation {

    @Id
    @GeneratedValue
    @Column(name = "reservation_id")
    private Long id;

    private LocalDateTime reservationTime;
    private int numberOfPeople;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "popup_restaurant_id")
    private PopupRestaurant popupRestaurant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @OneToMany(mappedBy = "reservation")
    private List<ReservationMenu> reservationMenus;

    @Builder
    public Reservation(LocalDateTime reservationTime, int numberOfPeople , PopupRestaurant popupRestaurant, Customer customer) {
        this.reservationTime = reservationTime;
        this.numberOfPeople=numberOfPeople;
        this.popupRestaurant=popupRestaurant;
        this.customer=customer;
    }
}
