package com.OnedayOwner.server.platform.reservation.entity;

import com.OnedayOwner.server.platform.popup.entity.PopupRestaurant;
import com.OnedayOwner.server.platform.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id")
    private Long id;

    private LocalDateTime reservationDateTime;
    private int numberOfPeople;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "popup_restaurant_id")
    private PopupRestaurant popupRestaurant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "reservation")
    private List<ReservationMenu> reservationMenus = new ArrayList<>();

    @Builder
    public Reservation(LocalDateTime reservationDateTime, int numberOfPeople , PopupRestaurant popupRestaurant, User user) {
        this.reservationDateTime = reservationDateTime;
        this.numberOfPeople=numberOfPeople;
        this.popupRestaurant=popupRestaurant;
        this.user = user;
    }

    public void addReservationMenu(ReservationMenu reservationMenu){
        this.reservationMenus.add(reservationMenu);
    }
}
