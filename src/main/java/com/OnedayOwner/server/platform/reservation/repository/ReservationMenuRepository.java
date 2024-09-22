package com.OnedayOwner.server.platform.reservation.repository;

import com.OnedayOwner.server.platform.reservation.entity.ReservationMenu;
import com.OnedayOwner.server.platform.reservation.repository.custom.ReservationMenuRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationMenuRepository extends JpaRepository<ReservationMenu, Long>, ReservationMenuRepositoryCustom {
}
