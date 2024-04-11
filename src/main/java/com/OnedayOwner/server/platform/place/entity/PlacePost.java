package com.OnedayOwner.server.platform.place.entity;

import com.OnedayOwner.server.global.model.BaseTimeEntity;
import jakarta.persistence.*;
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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_info_id")
    private PlaceInfo placeInfo;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_description_id")
    private PlaceDescription placeDescription;
}
