package com.OnedayOwner.server.platform.reservation.repository;


import com.OnedayOwner.server.platform.reservation.entity.ReservationTime;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationTimeRepository extends JpaRepository<ReservationTime, Long> {
}
