package com.OnedayOwner.server.platform.reservation.repository.custom;

import com.querydsl.core.Tuple;

import java.time.LocalDate;
import java.util.List;

public interface ReservationMenuRepositoryCustom {
    List<Tuple> getDailyMenuCountGroupByReservationTime(Long popupId, LocalDate date);
}
