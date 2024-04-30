package com.OnedayOwner.server.platform.popup.entity;

import com.OnedayOwner.server.global.model.BaseTimeEntity;
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
public class BusinessTime extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "business_time_id")
    private Long id;

    private LocalTime openTime;
    private LocalTime closeTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "popup_restaurant_id")
    private PopupRestaurant popupRestaurant;


    @Builder
    public BusinessTime(PopupRestaurant popupRestaurant, LocalTime openTime, LocalTime closeTime) {
        this.popupRestaurant = popupRestaurant;
        this.openTime = openTime;
        this.closeTime = closeTime;

        popupRestaurant.addBusinessTime(this);
    }

}
