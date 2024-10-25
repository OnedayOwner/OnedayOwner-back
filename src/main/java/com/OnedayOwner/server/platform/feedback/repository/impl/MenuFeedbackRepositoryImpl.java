package com.OnedayOwner.server.platform.feedback.repository.impl;

import com.OnedayOwner.server.platform.feedback.entity.MenuFeedback;
import com.OnedayOwner.server.platform.feedback.entity.QMenuFeedback;
import com.OnedayOwner.server.platform.feedback.repository.MenuFeedbackRepository;
import com.OnedayOwner.server.platform.feedback.repository.custom.MenuFeedbackRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import java.util.List;

import static com.OnedayOwner.server.platform.feedback.entity.QMenuFeedback.menuFeedback;

public class MenuFeedbackRepositoryImpl implements MenuFeedbackRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    public MenuFeedbackRepositoryImpl(EntityManager em){
        this.jpaQueryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<MenuFeedback> findByMenuId(Long menuId) {
        return jpaQueryFactory.selectFrom(menuFeedback)
                .where(menuFeedback.reservationMenu.menu.id.eq(menuId))
                .fetch();

    }
}
