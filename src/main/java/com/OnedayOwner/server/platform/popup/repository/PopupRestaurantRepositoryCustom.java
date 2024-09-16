package com.OnedayOwner.server.platform.popup.repository;

import com.OnedayOwner.server.platform.popup.entity.PopupRestaurant;

import java.util.List;
import java.util.Optional;

public interface PopupRestaurantRepositoryCustom {

    Optional<PopupRestaurant> getInBusinessPopupRestaurantWithMenusAndReservationTimesAndBusinessTimesByUserId(Long ownerId);

    Optional<PopupRestaurant> getPopupRestaurantWithMenusById(Long popupId);

    List<PopupRestaurant> findActivePopupRestaurantsWithMenus();

    List<PopupRestaurant> findFuturePopupRestaurantsWithMenus();
}
