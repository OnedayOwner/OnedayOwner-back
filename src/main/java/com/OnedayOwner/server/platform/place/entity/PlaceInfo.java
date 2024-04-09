package com.OnedayOwner.server.platform.place.entity;

import com.OnedayOwner.server.global.model.BaseTimeEntity;
import com.OnedayOwner.server.platform.Address;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class PlaceInfo extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "place_id")
    private Long id;

    private Address address;

    @Builder
    public PlaceInfo(Address address) {
        this.address=address;
    }
}
