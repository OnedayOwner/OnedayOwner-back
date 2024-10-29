package com.OnedayOwner.server.platform.feedback.repository.impl;

import com.OnedayOwner.server.platform.feedback.entity.Feedback;
import com.OnedayOwner.server.platform.feedback.entity.QFeedback;
import com.OnedayOwner.server.platform.feedback.repository.custom.FeedbackRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

import static com.OnedayOwner.server.platform.feedback.entity.QFeedback.feedback;
import static com.OnedayOwner.server.platform.popup.entity.QPopupRestaurant.popupRestaurant;
import static com.OnedayOwner.server.platform.reservation.entity.QReservation.*;
import static com.OnedayOwner.server.platform.user.entity.QUser.user;

public class FeedbackRepositoryImpl implements FeedbackRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    public FeedbackRepositoryImpl(EntityManager em) {
        this.jpaQueryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Optional<Feedback> findByIdFetchJoin(Long feedbackId){
        Feedback feedback = jpaQueryFactory.select(QFeedback.feedback)
                .from(QFeedback.feedback)
                .join(QFeedback.feedback.reservation, reservation).fetchJoin()
                .join(reservation.popupRestaurant, popupRestaurant).fetchJoin()
                .join(popupRestaurant.user, user).fetchJoin()
                .where(QFeedback.feedback.id.eq(feedbackId))
                .fetchOne();

        return Optional.ofNullable(feedback);
    }

    @Override
    public List<Feedback> findByUserIdFetchJoin(Long customerId){

        return jpaQueryFactory.select(QFeedback.feedback)
                .from(QFeedback.feedback)
                .join(QFeedback.feedback.reservation, reservation).fetchJoin()
                .join(reservation.popupRestaurant, popupRestaurant).fetchJoin()
                .join(popupRestaurant.user, user).fetchJoin()
                .where(QFeedback.feedback.user.id.eq(customerId))
                .fetch();
    }

    @Override
    public List<Feedback> findByPopupId(Long popupId) {
        return jpaQueryFactory.selectFrom(feedback)
                .where(feedback.reservation.popupRestaurant.id.eq(popupId))
                .fetch();
    }

    @Override
    public Long countByPopupId(Long popupId) {
        return jpaQueryFactory.selectFrom(feedback)
                .where(feedback.reservation.popupRestaurant.id.eq(popupId))
                .stream().count();
    }
}
