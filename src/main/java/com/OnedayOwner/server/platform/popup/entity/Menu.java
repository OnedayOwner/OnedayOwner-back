package com.OnedayOwner.server.platform.popup.entity;

import com.OnedayOwner.server.global.model.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Menu extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "menu_id")
    private Long id;

    private String name;
    private int price;
    private String description;
    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "popup_restaurant_id")
    private PopupRestaurant popupRestaurant;

    @Builder
    public Menu(String name, int price , String description, PopupRestaurant popupRestaurant, String imageUrl) {
        this.name = name;
        this.price=price;
        this.description=description;
        this.imageUrl=imageUrl;

        this.popupRestaurant = popupRestaurant;
        popupRestaurant.addMenu(this);
    }
}
