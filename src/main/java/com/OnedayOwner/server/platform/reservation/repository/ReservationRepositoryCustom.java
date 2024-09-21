package com.OnedayOwner.server.platform.reservation.repository;

import com.querydsl.core.Tuple;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepositoryCustom {

    List<Tuple> getMonthlyReservationInfo(
            LocalDate startDate, LocalDate endDate, Long popupId);
}
