package com.OnedayOwner.server.platform.user.entity;

import com.OnedayOwner.server.global.model.BaseTimeEntity;
import com.OnedayOwner.server.platform.place.entity.PlacePost;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlaceProvider extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "place_provider_id")
    private Long id;

    private String name;
    private String phoneNumber;

    private Gender gender;
    private LocalDate birth;

    @OneToMany(mappedBy = "placeProvider")
    private List<PlacePost> posts = new ArrayList<>();
}
