package com.OnedayOwner.server.platform.reservation.repository;

import com.OnedayOwner.server.platform.reservation.entity.ReservationTime;

import java.util.List;

public interface ReservationTimeRepositoryCustom {

    List<ReservationTime> getPossibleReservationTimes(Long id);
}
