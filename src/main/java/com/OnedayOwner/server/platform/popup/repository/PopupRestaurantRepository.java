package com.OnedayOwner.server.platform.popup.repository;

import com.OnedayOwner.server.platform.popup.entity.PopupRestaurant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PopupRestaurantRepository extends JpaRepository<PopupRestaurant, Long> {

    Optional<PopupRestaurant> findByOwnerIdAndInBusiness(Long ownerId, Boolean inBusiness);
    List<PopupRestaurant> findAllByOwnerIdAndInBusiness(Long ownerId, Boolean inBusiness);
}
