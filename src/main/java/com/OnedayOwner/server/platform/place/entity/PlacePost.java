package com.OnedayOwner.server.platform.place.entity;

import com.OnedayOwner.server.global.model.BaseTimeEntity;
import com.OnedayOwner.server.platform.Address;
import com.OnedayOwner.server.platform.user.entity.PlaceProvider;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class PlacePost extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "place_post_id")
    private Long id;

    private Address address;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_description_id")
    private PlaceDescription placeDescription;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="place_provider_id")
    private PlaceProvider placeProvider;

    @Builder
    public PlacePost(Address address, PlaceDescription placeDescription, PlaceProvider placeProvider) {
        this.address=address;
        this.placeDescription=placeDescription;
        this.placeProvider=placeProvider;
    }
}
