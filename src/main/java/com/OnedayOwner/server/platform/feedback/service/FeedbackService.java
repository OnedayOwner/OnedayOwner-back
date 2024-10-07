package com.OnedayOwner.server.platform.feedback.service;

import com.OnedayOwner.server.global.exception.BusinessException;
import com.OnedayOwner.server.global.exception.ErrorCode;
import com.OnedayOwner.server.platform.feedback.dto.FeedbackDto;
import com.OnedayOwner.server.platform.feedback.entity.MenuFeedback;
import com.OnedayOwner.server.platform.feedback.entity.Feedback;
import com.OnedayOwner.server.platform.feedback.repository.MenuFeedbackRepository;
import com.OnedayOwner.server.platform.feedback.repository.FeedbackRepository;
import com.OnedayOwner.server.platform.reservation.entity.Reservation;
import com.OnedayOwner.server.platform.reservation.repository.ReservationMenuRepository;
import com.OnedayOwner.server.platform.reservation.repository.ReservationRepository;
import com.OnedayOwner.server.platform.user.entity.User;
import com.OnedayOwner.server.platform.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final MenuFeedbackRepository menuFeedbackRepository;
    private final UserRepository userRepository;
    private final ReservationRepository reservationRepository;
    private final ReservationMenuRepository reservationMenuRepository;
    private final FeedbackRepository feedbackRepository;

    @Transactional
    public FeedbackDto.FeedbackDetail registerFeedback(Long userId, Long reservationId, FeedbackDto.FeedbackForm form){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESERVATION_NOT_FOUND));

        Feedback feedback = Feedback.builder()
                .reservation(reservation)
                .score(form.getScore())
                .comment(form.getComment())
                .user(user)
                .build();

        form.getMenuFeedBackForms()
                .forEach(
                        menuFeedBackForm -> {
                            MenuFeedback menuFeedback = MenuFeedback.builder()
                                    .score(menuFeedBackForm.getScore())
                                    .comment(menuFeedBackForm.getComment())
                                    .reservationMenu(reservationMenuRepository.findById(
                                            menuFeedBackForm.getReservationMenuId()).orElseThrow(
                                            () -> new BusinessException(ErrorCode.RESERVATION_MENU_NOT_FOUND)
                                    ))
                                    .desiredPrice(menuFeedBackForm.getDesiredPrice())
                                    .build();
                            feedback.addMenuFeedback(menuFeedback);
                        }
                );
        feedbackRepository.save(feedback);

        return new FeedbackDto.FeedbackDetail(feedback);
    }
}

