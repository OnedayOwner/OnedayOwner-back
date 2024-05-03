package com.OnedayOwner.server.platform.reservation.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.OnedayOwner.server.platform.popup.entity.QMenu.menu;
import static com.OnedayOwner.server.platform.popup.entity.QPopupRestaurant.popupRestaurant;
import static com.OnedayOwner.server.platform.reservation.entity.QReservation.reservation;

public class ReservationRepositoryImpl implements ReservationRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    public ReservationRepositoryImpl(EntityManager em) {
        this.jpaQueryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<LocalDateTime> findReservationTimesByCustomersGreaterThanMaxPeople(Long id, int maxPeople) {
        return jpaQueryFactory.select(reservation.reservationTime)
                .from(reservation)
                .leftJoin(reservation.popupRestaurant, popupRestaurant)
                .where(reservation.popupRestaurant.id.eq(id))
                .groupBy(reservation.reservationTime)
                .having(reservation.numberOfPeople.sum().goe(maxPeople))
                .fetch();
    }

    @Override
    public Integer findnumberOfPeopleByReservationTimeAndPopupId(Long id, LocalDateTime reservationTime) {
        return jpaQueryFactory.select(reservation.numberOfPeople.sum())
                .from(reservation)
                .leftJoin(reservation.popupRestaurant, popupRestaurant)
                .where(reservation.popupRestaurant.id.eq(id),
                        reservation.reservationTime.eq(reservationTime))
                .fetchOne();

    }
}
