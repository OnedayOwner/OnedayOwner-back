package com.OnedayOwner.server.platform.reservation.repository.custom;

import com.OnedayOwner.server.platform.reservation.entity.Reservation;
import com.querydsl.core.Tuple;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReservationRepositoryCustom {

    List<Tuple> getMonthlyReservationInfo(
            LocalDate startDate, LocalDate endDate, Long popupId);

    public List<Reservation> findUpcomingReservationsByUserId(Long userId);
    public List<Reservation> findCompletedReservationsByUserId(Long userId);
    public Optional<Reservation> findReservationWithDetails(Long reservationId);
}
