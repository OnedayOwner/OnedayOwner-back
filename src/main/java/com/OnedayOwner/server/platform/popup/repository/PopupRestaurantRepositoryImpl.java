package com.OnedayOwner.server.platform.popup.repository;

import com.OnedayOwner.server.platform.popup.entity.PopupRestaurant;
import com.OnedayOwner.server.platform.popup.entity.QBusinessTime;
import com.OnedayOwner.server.platform.popup.entity.QMenu;
import com.OnedayOwner.server.platform.popup.entity.QPopupRestaurant;
import com.OnedayOwner.server.platform.reservation.entity.QReservationTime;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import java.util.Optional;

import static com.OnedayOwner.server.platform.popup.entity.QBusinessTime.businessTime;
import static com.OnedayOwner.server.platform.popup.entity.QMenu.menu;
import static com.OnedayOwner.server.platform.popup.entity.QPopupRestaurant.*;
import static com.OnedayOwner.server.platform.reservation.entity.QReservationTime.*;

public class PopupRestaurantRepositoryImpl implements PopupRestaurantRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    public PopupRestaurantRepositoryImpl(EntityManager em) {
        this.jpaQueryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Optional<PopupRestaurant> getInBusinessPopupRestaurantWithMenusAndReservationTimesAndBusinessTimes(Long id) {
        return jpaQueryFactory.selectFrom(popupRestaurant)
                .leftJoin(popupRestaurant.menus, menu).fetchJoin()
//                .leftJoin(popupRestaurant.reservationTimes, reservationTime).fetchJoin()
//                .leftJoin(popupRestaurant.businessTimes, businessTime).fetchJoin()
                .where(popupRestaurant.id.eq(id),
                        popupRestaurant.inBusiness.eq(true))
                .fetch().stream().findFirst();
    }

    @Override
    public Optional<PopupRestaurant> getPopupRestaurantWithMenus(Long id) {
        return jpaQueryFactory.selectFrom(popupRestaurant)
                .leftJoin(popupRestaurant.menus).fetchJoin()
                .where(popupRestaurant.id.eq(id))
                .fetch().stream().findFirst();
    }


}
