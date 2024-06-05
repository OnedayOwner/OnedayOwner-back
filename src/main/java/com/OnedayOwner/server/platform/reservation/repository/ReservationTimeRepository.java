package com.OnedayOwner.server.platform.reservation.repository;


import com.OnedayOwner.server.platform.reservation.entity.ReservationTime;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalTime;

public interface ReservationTimeRepository extends JpaRepository<ReservationTime, Long>, ReservationTimeRepositoryCustom {

    Boolean existsByStartTimeAndPopupRestaurantId(LocalTime startTime, Long id);
}
