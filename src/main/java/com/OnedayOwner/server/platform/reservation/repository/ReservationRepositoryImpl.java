package com.OnedayOwner.server.platform.reservation.repository;

import com.OnedayOwner.server.platform.reservation.entity.Reservation;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import java.time.LocalDateTime;
import java.util.List;

import static com.OnedayOwner.server.platform.popup.entity.QMenu.menu;
import static com.OnedayOwner.server.platform.popup.entity.QPopupRestaurant.popupRestaurant;
import static com.OnedayOwner.server.platform.reservation.entity.QReservation.reservation;

public class ReservationRepositoryImpl implements ReservationRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    public ReservationRepositoryImpl(EntityManager em) {
        this.jpaQueryFactory = new JPAQueryFactory(em);
    }

    // 방문 예정 예약 찾기
    @Override
    public List<Reservation> findUpcomingReservationsByUserId(Long userId) {
        return jpaQueryFactory.selectFrom(reservation)
                .join(reservation.popupRestaurant, popupRestaurant).fetchJoin()
                .where(reservation.user.id.eq(userId)
                        .and(reservation.reservationDateTime.after(LocalDateTime.now())))
                .fetch();
    }

    // 방문 완료 예약 찾기
    @Override
    public List<Reservation> findCompletedReservationsByUserId(Long userId) {
        return jpaQueryFactory.selectFrom(reservation)
                .join(reservation.popupRestaurant, popupRestaurant).fetchJoin()
                .where(reservation.user.id.eq(userId)
                        .and(reservation.reservationDateTime.before(LocalDateTime.now())))
                .fetch();
    }
}
