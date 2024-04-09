package com.OnedayOwner.server.platform.place.entity;

import com.OnedayOwner.server.global.model.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class PlaceDescription extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "place_description_id")
    private Long id;

    private String name;
    private int size;
    private int maximumNumberOfPeople;

    private int price;

    private LocalDate startDate;
    private LocalDate endDate;

    private String description;

    @Builder
    public PlaceDescription(String name, int size, int maximumNumberOfPeople, int price, LocalDate startDate, LocalDate endDate, String description) {
        this.name=name;
        this.size=size;
        this.maximumNumberOfPeople=maximumNumberOfPeople;
        this.price=price;
        this.startDate=startDate;
        this.endDate=endDate;
        this.description=description;
    }
}
