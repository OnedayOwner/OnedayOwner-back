package com.OnedayOwner.server.platform.feedback.repository.impl;

import com.OnedayOwner.server.platform.feedback.dto.FeedbackDto;
import com.OnedayOwner.server.platform.feedback.entity.Feedback;
import com.OnedayOwner.server.platform.feedback.entity.QFeedback;
import com.OnedayOwner.server.platform.feedback.repository.custom.FeedbackRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import java.util.List;

import static com.OnedayOwner.server.platform.feedback.entity.QFeedback.feedback;

public class FeedbackRepositoryImpl implements FeedbackRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    public FeedbackRepositoryImpl(EntityManager em) {
        this.jpaQueryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<Feedback> findByPopupId(Long popupId) {
        return jpaQueryFactory.selectFrom(feedback)
                .where(feedback.reservation.popupRestaurant.id.eq(popupId))
                .fetch();
    }
}
