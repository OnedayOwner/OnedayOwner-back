package com.OnedayOwner.server.platform.popup.entity;

import com.OnedayOwner.server.global.model.BaseTimeEntity;
import com.OnedayOwner.server.platform.Address;
import com.OnedayOwner.server.platform.place.entity.PlaceInfo;
import com.OnedayOwner.server.platform.reservation.entity.ReservationTime;
import com.OnedayOwner.server.platform.user.entity.Owner;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PopupRestaurant extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "popup_restaurant_id")
    private Long id;

    private String name;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;


    @OneToMany(mappedBy = "popupRestaurant", cascade = CascadeType.ALL)
    private List<Menu> menus = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private Owner owner;

    private Address address;
    private Boolean inBusiness;
    private String description;

    @OneToMany(mappedBy = "popupRestaurant", cascade = CascadeType.ALL)
    private List<BusinessTime> businessTimes = new ArrayList<>();

    @OneToMany(mappedBy = "popupRestaurant", cascade = CascadeType.ALL)
    private List<ReservationTime> reservationTimes = new ArrayList<>();

//    @Enumerated(EnumType.STRING)
//    private Category category;

    public void addBusinessTime(BusinessTime businessTime){
        this.businessTimes.add(businessTime);
    }

    public void addReservationTime(ReservationTime reservationTime){
        this.reservationTimes.add(reservationTime);
    }

    public void addMenu(Menu menu){
        this.menus.add(menu);
    }

    public void close(){
        this.inBusiness = false;
    }

    @Builder
    public PopupRestaurant(String name, LocalDateTime startDateTime, LocalDateTime endDateTime, Owner owner, Address address, String description){
        this.name = name;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.owner = owner;
        this.address = address;
//        this.category = category;
        this.inBusiness = true;
        this.description = description;
    }
}
