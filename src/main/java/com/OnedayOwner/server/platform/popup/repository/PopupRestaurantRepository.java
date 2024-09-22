package com.OnedayOwner.server.platform.popup.repository;

import com.OnedayOwner.server.platform.popup.entity.PopupRestaurant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PopupRestaurantRepository extends JpaRepository<PopupRestaurant, Long>, PopupRestaurantRepositoryCustom {

    Optional<PopupRestaurant> findByUserIdAndInBusiness(Long ownerId, Boolean inBusiness);
    List<PopupRestaurant> findAllByUserIdAndInBusiness(Long ownerId, Boolean inBusiness);
    List<PopupRestaurant> findAllByInBusiness(Boolean inBusiness);

    void deleteByUserIdAndId(Long ownerId, Long popupId);
}
