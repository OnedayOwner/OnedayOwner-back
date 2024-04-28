package com.OnedayOwner.server.platform.popup.repository;

import com.OnedayOwner.server.platform.popup.entity.PopupRestaurant;
import com.OnedayOwner.server.platform.popup.entity.QPopupRestaurant;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import java.util.Optional;

import static com.OnedayOwner.server.platform.popup.entity.QPopupRestaurant.*;

public class PopupRestaurantRepositoryImpl implements PopupRestaurantRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    public PopupRestaurantRepositoryImpl(EntityManager em) {
        this.jpaQueryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Optional<PopupRestaurant> getInBusinessPopupRestaurantWithMenusAndReservationTimesAndBusinessTimes(Long id) {
        return jpaQueryFactory.selectFrom(popupRestaurant)
                .leftJoin(popupRestaurant.menus).fetchJoin()
                .leftJoin(popupRestaurant.reservationTimes).fetchJoin()
                .leftJoin(popupRestaurant.businessTimes).fetchJoin()
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
