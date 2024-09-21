package com.OnedayOwner.server.platform.reservation.repository;

import com.OnedayOwner.server.platform.reservation.entity.QReservation;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import java.time.LocalDate;
import java.util.List;

import static com.OnedayOwner.server.platform.reservation.entity.QReservation.reservation;

public class ReservationRepositoryImpl implements ReservationRepositoryCustom{
    private final JPAQueryFactory jpaQueryFactory;

    public ReservationRepositoryImpl(EntityManager em) {
        this.jpaQueryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<Tuple> getMonthlyReservationInfo(
            LocalDate startDate, LocalDate endDate, Long popupId){

        return jpaQueryFactory.select(reservation.reservationDateTime.dayOfMonth(),
                        reservation.count(),
                        reservation.numberOfPeople.sum())
                .from(reservation)
                .where(reservation.reservationDateTime
                        .between(startDate.atStartOfDay(),endDate.plusDays(1).atStartOfDay()))
                .groupBy(reservation.reservationDateTime.dayOfMonth())
                .fetch();

    }
}
