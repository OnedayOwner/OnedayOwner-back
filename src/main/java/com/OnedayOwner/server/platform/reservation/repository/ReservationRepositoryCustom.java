package com.OnedayOwner.server.platform.reservation.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReservationRepositoryCustom {

    List<LocalDateTime> findReservationTimesByCustomersGreaterThanMaxPeople(Long id, int maxPeople);
    Optional<Integer> findNumberOfPeopleByReservationTimeAndPopupId(Long id, LocalDateTime reservationTime);
}