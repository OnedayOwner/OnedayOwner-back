package com.OnedayOwner.server.platform.reservation.repository.impl;

import com.OnedayOwner.server.platform.reservation.entity.Reservation;
import com.OnedayOwner.server.platform.reservation.repository.custom.ReservationRepositoryCustom;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.OnedayOwner.server.platform.feedback.entity.QFeedback.feedback;
import static com.OnedayOwner.server.platform.popup.entity.QMenu.menu;
import static com.OnedayOwner.server.platform.popup.entity.QPopupRestaurant.popupRestaurant;
import static com.OnedayOwner.server.platform.reservation.entity.QReservation.reservation;
import static com.OnedayOwner.server.platform.reservation.entity.QReservationMenu.reservationMenu;

public class ReservationRepositoryImpl implements ReservationRepositoryCustom {
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

    // 예약 상세 내역 찾기
    @Override
    public Optional<Reservation> findReservationWithDetails(Long reservationId) {
        return jpaQueryFactory.selectFrom(reservation)
                .leftJoin(reservation.popupRestaurant, popupRestaurant).fetchJoin()
                .leftJoin(reservation.reservationMenus, reservationMenu).fetchJoin()
                .leftJoin(reservationMenu.menu, menu).fetchJoin()
                .where(reservation.id.eq(reservationId))
                .fetch().stream().findFirst();
    }

    @Override
    public List<Reservation> findCompletedReservationsWithoutFeedbackByUserId(Long userId) {
        return jpaQueryFactory.selectFrom(reservation)
                .leftJoin(feedback).on(feedback.reservation.eq(reservation))
                .where(
                        reservation.user.id.eq(userId),
                        reservation.reservationDateTime.before(LocalDateTime.now()),
                        feedback.isNull()
                )
                .fetch();
    }
}
