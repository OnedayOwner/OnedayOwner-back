package com.OnedayOwner.server.platform.popup.repository;

import com.OnedayOwner.server.platform.popup.entity.PopupRestaurant;
import com.OnedayOwner.server.platform.popup.entity.QBusinessTime;
import com.OnedayOwner.server.platform.popup.entity.QMenu;
import com.OnedayOwner.server.platform.popup.entity.QPopupRestaurant;
import com.OnedayOwner.server.platform.reservation.entity.QReservationTime;
import com.OnedayOwner.server.platform.reservation.entity.ReservationTime;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
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
    public Optional<PopupRestaurant> getInBusinessPopupRestaurantWithMenusAndReservationTimesAndBusinessTimesByUserId(Long ownerId) {
        return jpaQueryFactory.selectFrom(popupRestaurant)
                .leftJoin(popupRestaurant.menus, menu).fetchJoin()
//                .leftJoin(popupRestaurant.reservationTimes, reservationTime).fetchJoin()
//                .leftJoin(popupRestaurant.businessTimes, businessTime).fetchJoin()
                .where(popupRestaurant.user.id.eq(ownerId),
                        popupRestaurant.inBusiness.eq(true))
                .fetch().stream().findFirst();
    }

    @Override
    public Optional<PopupRestaurant> getPopupRestaurantWithMenusById(Long popupId) {
        return jpaQueryFactory.selectFrom(popupRestaurant)
                .leftJoin(popupRestaurant.menus).fetchJoin()
                .where(popupRestaurant.id.eq(popupId))
                .fetch().stream().findFirst();
    }

    @Override
    public List<PopupRestaurant> findActivePopupRestaurantsWithMenus() {
        LocalDateTime now = LocalDateTime.now();

        return jpaQueryFactory
                .selectFrom(popupRestaurant)
                .leftJoin(popupRestaurant.menus, menu).fetchJoin()
                .where(
                        popupRestaurant.startDateTime.loe(now)
                                .and(popupRestaurant.endDateTime.goe(now))
                                .and(popupRestaurant.inBusiness.isTrue())
                )
                .fetch();
    }

    @Override
    public List<PopupRestaurant> findFuturePopupRestaurantsWithMenus() {
        LocalDateTime now = LocalDateTime.now();

        return jpaQueryFactory
                .selectFrom(popupRestaurant)
                .leftJoin(popupRestaurant.menus, menu).fetchJoin()
                .where(
                        popupRestaurant.startDateTime.goe(now)
                                .and(popupRestaurant.inBusiness.isTrue())
                )
                .fetch();
    }
}
