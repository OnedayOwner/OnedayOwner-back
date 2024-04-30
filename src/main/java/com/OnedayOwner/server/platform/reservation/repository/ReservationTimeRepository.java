package com.OnedayOwner.server.platform.reservation.repository;


import com.OnedayOwner.server.platform.reservation.entity.ReservationTime;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface ReservationTimeRepository extends JpaRepository<ReservationTime, Long> {

    List<ReservationTime> findAllByPopupRestaurantId(Long id);
}
