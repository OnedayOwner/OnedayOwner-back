package com.OnedayOwner.server.platform.reservation.repository.impl;

import com.OnedayOwner.server.platform.reservation.repository.custom.ReservationMenuRepositoryCustom;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import java.time.LocalDate;
import java.util.List;

import static com.OnedayOwner.server.platform.reservation.entity.QReservationMenu.reservationMenu;

public class ReservationMenuRepositoryImpl implements ReservationMenuRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    public ReservationMenuRepositoryImpl (EntityManager em) {
        this.jpaQueryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<Tuple> getDailyMenuCountGroupByReservationTime(Long popupId, LocalDate date){
        return jpaQueryFactory.select(
                reservationMenu.menu.name,
                reservationMenu.quantity.sum(),
                reservationMenu.reservation.reservationDateTime
        )
                .from(reservationMenu)
//                .leftJoin(reservationMenu.reservation, reservation)
                .where(reservationMenu.reservation.popupRestaurant.id.eq(popupId),
                        reservationMenu.reservation.reservationDateTime.between(
                        date.atStartOfDay(), date.plusDays(1).atStartOfDay()
                ))
                .groupBy(reservationMenu.reservation.reservationDateTime
                        ,reservationMenu.menu.name
                        )
                .fetch();
    }

}
