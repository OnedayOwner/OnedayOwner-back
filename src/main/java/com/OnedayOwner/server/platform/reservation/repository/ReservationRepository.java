package com.OnedayOwner.server.platform.reservation.repository;

import com.OnedayOwner.server.platform.reservation.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findAllByUserId(Long userId);
}
