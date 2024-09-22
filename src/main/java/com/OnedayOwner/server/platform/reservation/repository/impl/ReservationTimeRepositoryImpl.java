package com.OnedayOwner.server.platform.reservation.repository.impl;

import com.OnedayOwner.server.platform.reservation.entity.ReservationTime;
import com.OnedayOwner.server.platform.reservation.repository.custom.ReservationTimeRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import java.time.LocalDate;
import java.util.List;

import static com.OnedayOwner.server.platform.popup.entity.QPopupRestaurant.popupRestaurant;
import static com.OnedayOwner.server.platform.reservation.entity.QReservationTime.reservationTime;

public class ReservationTimeRepositoryImpl implements ReservationTimeRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    public ReservationTimeRepositoryImpl(EntityManager em) {
        this.jpaQueryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<ReservationTime> getPossibleReservationTimes(Long id) {
        return jpaQueryFactory.selectFrom(reservationTime)
                .leftJoin(reservationTime.popupRestaurant, popupRestaurant)
                .where(reservationTime.popupRestaurant.id.eq(id),
                        reservationTime.maxPeople.gt(0),
                        reservationTime.reservationDate.gt(LocalDate.now()))
                .fetch();
    }
}
