package com.OnedayOwner.server.platform.reservation.entity;

import com.OnedayOwner.server.platform.popup.entity.Menu;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReservationMenu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_menu_id")
    private Long id;

    private int quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    private Menu menu;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    @Builder
    public ReservationMenu(int quantity, Menu menu , Reservation reservation) {
        this.quantity = quantity;
        this.menu=menu;
        this.reservation=reservation;

        reservation.addReservationMenu(this);
    }
}
