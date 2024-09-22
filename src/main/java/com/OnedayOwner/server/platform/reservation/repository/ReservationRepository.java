package com.OnedayOwner.server.platform.reservation.repository;

import com.OnedayOwner.server.platform.reservation.entity.Reservation;
import com.OnedayOwner.server.platform.reservation.repository.custom.ReservationRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long>, ReservationRepositoryCustom {
    List<Reservation> findAllByUserId(Long userId);
    int countAllByPopupRestaurantId(Long popupId);

    @Query("select count(r), sum(r.numberOfPeople) from Reservation r where r.popupRestaurant.id = :popupId")
    List<Long[]> sumNumberOfPeopleByPopupRestaurantId(Long popupId);

}
