package com.OnedayOwner.server.platform.reservation.repository;

import com.OnedayOwner.server.platform.reservation.entity.Reservation;

import java.util.List;

public interface ReservationRepositoryCustom {
    public List<Reservation> findUpcomingReservationsByUserId(Long userId);
    public List<Reservation> findCompletedReservationsByUserId(Long userId);
}
