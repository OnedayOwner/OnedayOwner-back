package com.OnedayOwner.server.platform.reservation.repository;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepositoryCustom {

    List<LocalDateTime> findReservationTimesByCustomersGreaterThanMaxPeople(Long id, int maxPeople);
}
