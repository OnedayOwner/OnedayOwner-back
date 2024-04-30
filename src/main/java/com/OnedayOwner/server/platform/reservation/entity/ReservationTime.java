package com.OnedayOwner.server.platform.reservation.entity;

import com.OnedayOwner.server.global.model.BaseTimeEntity;
import com.OnedayOwner.server.platform.popup.entity.PopupRestaurant;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReservationTime extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_time_id")
    private Long id;

    private LocalTime startTime;
    private LocalTime endTime;
    private int maxPeople;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "popup_restaurant_id")
    private PopupRestaurant popupRestaurant;

    @Builder
    public ReservationTime(PopupRestaurant popupRestaurant, LocalTime startTime, LocalTime endTime, int maxPeople) {
        this.popupRestaurant = popupRestaurant;
        this.startTime = startTime;
        this.endTime = endTime;
        this.maxPeople = maxPeople;

        popupRestaurant.addReservationTime(this);
    }

}
