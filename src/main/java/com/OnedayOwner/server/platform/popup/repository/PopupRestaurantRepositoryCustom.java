package com.OnedayOwner.server.platform.popup.repository;

import com.OnedayOwner.server.platform.popup.entity.PopupRestaurant;

import java.util.Optional;

public interface PopupRestaurantRepositoryCustom {

    Optional<PopupRestaurant> getInBusinessPopupRestaurantWithMenusAndReservationTimesAndBusinessTimesByUserId(Long ownerId);

    Optional<PopupRestaurant> getPopupRestaurantWithMenusById(Long popupId);
}
